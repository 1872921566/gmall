package com.zxshare.gmall.gmallcartweb.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zxshare.gmall.anotations.LoginRequired;
import com.zxshare.gmall.bean.OmsCartItem;
import com.zxshare.gmall.bean.PmsSkuInfo;
import com.zxshare.gmall.service.CartService;
import com.zxshare.gmall.service.SkuService;
import com.zxshare.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sir
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;


    @LoginRequired(loginSuccess = false)
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        String userId = "1";

        if (StringUtils.isNotBlank(userId)) {
            //登录状态  查询DB/Cache
            omsCartItems = cartService.cartList(userId);
        } else {
            //未登录状态 查询Cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }


        }

        modelMap.put("cartList", omsCartItems);
        return "cartList";
    }

    @LoginRequired(loginSuccess = false)
    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        //调用商品服务查询商品信息
        PmsSkuInfo skuById = skuService.getSkuById(skuId);
        //封装成购物车对象
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setQuantity(new BigDecimal(quantity));
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setProductName(skuById.getSkuName());
        omsCartItem.setProductSkuCode("1111111111111");
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setProductBrand("");
        omsCartItem.setIsChecked("1");


        //判断用户是否登录
        String memberId = "1";

        if (StringUtils.isNotBlank(memberId)) {
            //登录状态 DB/Cache
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId, skuId);

            if (omsCartItemFromDb == null) {
                //该用户没有添加商品
                omsCartItem.setMemberId(memberId);

                cartService.addCart(omsCartItem);
            } else {
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));

                cartService.updateCart(omsCartItemFromDb);
            }
            //同步缓存
//            cartService.flushCartCache(memberId);

        } else {
            //未登录状态====Cookie/跨域问题
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);

            if (StringUtils.isBlank(cartListCookie)) {
                //cookie 为空
                omsCartItems.add(omsCartItem);
            } else {
                //cookie 不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //判断Cookie中商品是否重复
                Boolean exist = if_cart_exist(omsCartItems, omsCartItem);
                if (exist) {
                    //如果重复，更新商品数据
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {

                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));

                        }
                    }


                } else {
                    //如果不重复，添加商品
                    omsCartItems.add(omsCartItem);
                }

            }

            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);

        }

        return "redirect:/success.html";
    }

    private Boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        Boolean exist = false;

        for (OmsCartItem cartItem : omsCartItems) {
            if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                exist = true;
            }
        }

        return exist;
    }


}
