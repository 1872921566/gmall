package com.zxshare.gmall.service;

import com.zxshare.gmall.service.util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class GmallManagerServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() {
        Jedis jedis = redisUtil.getJedis();
        System.out.println(jedis);

    }

}
