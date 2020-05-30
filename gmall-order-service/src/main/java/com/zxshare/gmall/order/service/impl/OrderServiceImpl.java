package com.zxshare.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.service.OrderService;
import com.zxshare.gmall.service.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author sir
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String getTradeCode(String menBerId) {
        String TradeCode = "";
        Jedis jedis = redisUtil.getJedis();


        try {
            String key = "user:" + menBerId + ":TradeCode";

            TradeCode = UUID.randomUUID().toString();

            jedis.setex(key, 60 * 30, TradeCode);


        } finally {
            jedis.close();
        }
        return TradeCode;
    }

    @Override
    public Boolean checkTradeCode(String menBerId, String TradeCode) {

        Jedis jedis = redisUtil.getJedis();

        try {
            if (StringUtils.isNotBlank(TradeCode) && StringUtils.isNotBlank(menBerId)) {
                String s = jedis.get("user:" + menBerId + ":TradeCode");

                if (TradeCode.equals(s)) {
                    //校验成功后需要销毁code
                    return true;
                }

            }


        } finally {

            jedis.close();
        }

        return false;
    }
}
