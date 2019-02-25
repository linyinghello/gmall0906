package com.linying.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.UserAddress;
import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.mamage.util.RedisUtil;
import com.linying.gmall.service.UserService;
import com.linying.gmall.user.mapper.UserAddressMapper;
import com.linying.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public UserInfo getUserInfo(UserInfo userInfo) {

        UserInfo userInfoFromBD = userInfoMapper.selectOne(userInfo);
        return userInfoFromBD;
    }

    @Override
    public void setUserInfoCache(UserInfo userInfo) {
        String userId = userInfo.getId();
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+userId+":info",60*60*24, JSON.toJSONString(userInfo));
        jedis.close();

    }

    @Override
    public List<UserAddress> getAddressByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> userAddresss = userAddressMapper.select(userAddress);

        return userAddresss;
    }

    @Override
    public UserAddress getAddressById(String deliverAddressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(deliverAddressId);
        UserAddress userAddressFromDb = userAddressMapper.selectOne(userAddress);
        return userAddressFromDb;
    }
}
