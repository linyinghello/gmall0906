package com.linying.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.user.mapper.UserInfoMapper;
import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> getUserInfoList() {
        return userInfoMapper.selectAll();
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        UserInfo userInfo1 = userInfoMapper.selectOne(userInfo);
        System.out.println(userInfo1);
        return userInfo1;
    }
}
