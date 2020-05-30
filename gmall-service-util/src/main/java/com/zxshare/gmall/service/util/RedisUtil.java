package com.zxshare.gmall.service.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisUtil {

    private JedisPool jedisPool;

    public void initPool(String host,int port,int database){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(30);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(10*1000);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, host, port, 10 * 1000);


    }

    public Jedis getJedis(){
        Jedis resource = jedisPool.getResource();
        return resource;
    }
}
