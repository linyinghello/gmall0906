package com.linying.gmall.service;

import com.linying.gmall.bean.UserAddress;
import com.linying.gmall.bean.UserInfo;

import java.util.List;

public interface UserService {
    UserInfo getUserInfo(UserInfo userInfo);
    void setUserInfoCache(UserInfo userInfo);

    List<UserAddress> getAddressByUserId(String userId);

    UserAddress getAddressById(String deliverAddressId);
}
