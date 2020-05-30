package com.zxshare.gmall.gmallcartservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.bean.OmsCartItem;
import com.zxshare.gmall.gmallcartservice.mapper.OmsCartItemMapper;
import com.zxshare.gmall.service.CartService;
import com.zxshare.gmall.service.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author sir
 */

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);

        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {

        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            omsCartItemMapper.insert(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());

        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb, example);
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        //
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);

        List<OmsCartItem> select = omsCartItemMapper.select(omsCartItem);
        return select;
    }


    //redis
//    @Override
//    public List<OmsCartItem> cartList(String userId) {
//        Jedis jedis = redisUtil.getJedis();
//        List<OmsCartItem> omsCartItems = null;
//        String Json = jedis.get("user:" + userId + ":cart");
//
//        if (StringUtils.isNotBlank(Json)){
//            Map<String,String> map = JSON.parseObject(Json, HashMap.class);
//        }
//
//        jedis.close();
//        return null;
//    }

//    //同步缓存
//    @Override
//    public void flushCartCache(String memberId) {
//
//        OmsCartItem omsCartItem = new OmsCartItem();
//        omsCartItem.setMemberId(memberId);
//        List<OmsCartItem> select = omsCartItemMapper.select(omsCartItem);
//
//        //同步缓存
//        Jedis jedis = redisUtil.getJedis();
//
//        Map<String, String> map = new HashMap<>(16);
//        for (OmsCartItem cartItem : select) {
//            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
//        }
//        jedis.hmset("user:" + memberId + ":cart", map);
//
//        jedis.close();
//    }


}
