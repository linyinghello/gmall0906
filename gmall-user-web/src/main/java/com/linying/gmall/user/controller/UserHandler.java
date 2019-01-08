package com.linying.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
public class UserHandler {

    @Reference
    private UserInfoService userInfoService;

  @RequestMapping("userInfoList")
  @ResponseBody
    public List<UserInfo> getUserInfoList() {

        List<UserInfo> userInfos =  userInfoService.getUserInfoList();
            return userInfos;
    }
}
