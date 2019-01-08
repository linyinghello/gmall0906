package com.linying.gmall.user.service.Impl;

import com.linying.gmall.user.mapper.UserInfoMapper;
import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> getUserInfoList() {
        return userInfoMapper.selectAll();
    }
}
