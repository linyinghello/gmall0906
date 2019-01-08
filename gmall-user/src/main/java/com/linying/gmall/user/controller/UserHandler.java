package com.linying.gmall.user.controller;

import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserHandler {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("userInfoList")
    public List<UserInfo> getUserInfoList() {
    return userInfoService.getUserInfoList();
    }
}
