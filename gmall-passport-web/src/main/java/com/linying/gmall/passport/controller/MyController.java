package com.linying.gmall.passport.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.bean.UserInfo;
import com.linying.gmall.service.CartService;
import com.linying.gmall.service.UserService;
import com.linying.gmall.util.CookieUtil;

import com.linying.gmall.util.JwtUtil;
import com.linying.gmall.utils.CrowdfundingStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyController {

    @Reference
    UserService userService;

    @Reference
    CartService cartService;

    @RequestMapping("verify")
    @ResponseBody
    public String varify(HttpServletRequest request,String token,String requestIp,Model model){

        Map linying = JwtUtil.decode("linying", token, CrowdfundingStringUtils.md5(requestIp));
        System.out.println(linying);
        if(linying !=null){
            String userId=(String) linying.get("userId");

            return userId;

        }else {
            return null;
        }
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, Model model, HttpServletRequest request, HttpServletResponse response){

      UserInfo userInfoFromDB =   userService.getUserInfo(userInfo);
      userService.setUserInfoCache(userInfoFromDB);
      String token = "";
      if(userInfoFromDB==null){
          return "err";
      } else {
          HashMap<String, String> stringStringHashMap = new HashMap<>();
          stringStringHashMap.put("userId",userInfoFromDB.getId());
          stringStringHashMap.put("nickName",userInfoFromDB.getNickName());
          String nip = request.getHeader("request-forwared-for");
          if(StringUtils.isBlank(nip)){
              nip = request.getRemoteAddr();
              if(StringUtils.isBlank(nip)){
                  nip = "127.0.0.1";
              }
          }

        //  JwtUtil.encode("linying",stringStringHashMap,"")
        token =   JwtUtil.encode("linying",stringStringHashMap, CrowdfundingStringUtils.md5(nip));

          String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
          String userId = userInfoFromDB.getId();

          cartService.updateCookieAndDb(userId,listCartCookieStr);

          CookieUtil.deleteCookie(request,response,"listCartCookie");

      }
        return token;
    }



    @RequestMapping("index")
    public String toIndex(String returnUrl,Model model){
        model.addAttribute("originUrl",returnUrl);
        return "index";
    }


}
