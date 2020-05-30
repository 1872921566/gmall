package com.zxshare.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.zxshare.gmall.bean.UmsMember;
import com.zxshare.gmall.bean.UmsMemberReceiveAddress;
import com.zxshare.gmall.service.UserService;
import com.zxshare.gmall.service.util.RedisUtil;
import com.zxshare.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.zxshare.gmall.user.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper receiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList = userMapper.selectAllUser();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress temp = new UmsMemberReceiveAddress();
        temp.setMemberId(memberId);
        return receiveAddressMapper.select(temp);
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String s = jedis.get("user:" + umsMember.getUsername() + "," + umsMember.getPassword() + ":info");

                if (StringUtils.isNotBlank(s)) {
                    UmsMember umsMemberFromCache = JSON.parseObject(s, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            UmsMember umsMemberFromDB = LoginFromDb(umsMember);
            if (umsMemberFromDB != null) {
                jedis.setex("user:" + umsMember.getUsername() + "," + umsMember.getPassword() + ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDB));
            }
            return umsMemberFromDB;

        } finally {
            jedis.close();
        }


    }

    @Override
    public void addUserToken(String token, String id) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+id+":token",60*60*2,token);

        jedis.close();
    }

    private UmsMember LoginFromDb(UmsMember umsMember) {
        List<UmsMember> select = userMapper.select(umsMember);
        if (select != null) {
            return select.get(0);
        }
        return null;
    }
}
