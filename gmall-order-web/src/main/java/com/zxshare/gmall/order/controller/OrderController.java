package com.zxshare.gmall.order.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zxshare.gmall.anotations.LoginRequired;
import com.zxshare.gmall.bean.OmsCartItem;
import com.zxshare.gmall.bean.OmsOrderItem;
import com.zxshare.gmall.bean.UmsMemberReceiveAddress;
import com.zxshare.gmall.service.CartService;
import com.zxshare.gmall.service.OrderService;
import com.zxshare.gmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sir
 */
@Controller
public class OrderController {


    @Reference
    UserService userService;

    @Reference
    CartService cartService;

    @Reference
    OrderService orderService;

    @RequestMapping("/submitOrder")
    @LoginRequired(loginSuccess = true)
    public String submitOrder(String receiveAddressId, BigDecimal totalAmount, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        //订单提交

        //检查交易码

        //根据用户id获得商品列表（购物车）和 总价格


        //将订单数据封装后写入数据库

        //将购物车数据删除订单中对应的商品 （选中状态）
        return "";
    }


    @RequestMapping("/toTrade")
    // 调试发现未拦截此方法
    @LoginRequired(loginSuccess = true)
    public String to(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        List<UmsMemberReceiveAddress> receiveAddressByMemberId = null;
        List<OmsCartItem> omsCartItems = null;
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("memberId");

        if (StringUtils.isNotBlank(memberId)) {
            receiveAddressByMemberId = userService.getReceiveAddressByMemberId(memberId);
            omsCartItems = cartService.cartList(memberId);
        }

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();

        for (OmsCartItem omsCartItem : omsCartItems) {
            if ("1".equals(omsCartItem.getIsChecked())) {
                OmsOrderItem param = new OmsOrderItem();
                param.setProductName(omsCartItem.getProductName());
                param.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(param);
            }

        }
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            BigDecimal productPrice = omsOrderItem.getProductPrice();
            totalAmount.add(productPrice);
        }

        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("receiveAddressByMemberId", receiveAddressByMemberId);
        modelMap.put("totalAmount", totalAmount);
        return "trade";
    }
}
