package com.linying.gmall.passport.test;

import com.linying.gmall.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

public class TestJWT {
    public static void main(String[] args) {
      String token = "eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6ImplcnJ5IiwidXNlcklkIjoiMiJ9.rHz-8lhSJPKTRHUu7z30UgZ34jxoOi9leqL_P4pht7g";
        Map linying = JwtUtil.decode("linying", token, "192.168.75.75");
        System.out.println(linying);
        createToken();
    }

    public static  void createToken(){
       HashMap<Object,Object>  objectObjectHashMap= new HashMap<>();
       objectObjectHashMap.put("userId","2");
       objectObjectHashMap.put("nickName","jerry");
       String token = JwtUtil.encode("linying",objectObjectHashMap,"192.168.75.75");
       System.out.println(token);
    }
}
