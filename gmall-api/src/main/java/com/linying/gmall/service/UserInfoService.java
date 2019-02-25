package com.linying.gmall.service;

import com.linying.gmall.bean.UserInfo;

import java.util.List;

public interface UserInfoService {
    List<UserInfo> getUserInfoList();

    UserInfo getUserInfoByUserId(String userId);
}
