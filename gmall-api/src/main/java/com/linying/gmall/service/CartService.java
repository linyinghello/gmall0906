package com.linying.gmall.service;

import com.linying.gmall.bean.CartInfo;

import java.util.List;

public interface CartService {

    CartInfo ifExists(String userId, String skuId);

    void updateCartInfo(CartInfo cartInfoByUserIdAndSkuId);

    void setCartInfo(CartInfo cartInfo);

    List<CartInfo> getCartInfosByUserId(String userId);

    void updateCartInfoChecked(CartInfo cartInfo);

    void decNumAndCartprice(String skuId,String userId);
    void addNumAndCartprice(String skuId,String userId);

    void flushCartCacheByUser(String userId);
    List<CartInfo> cartListFromCache(String userId);
   void updateCookieAndDb(String userId,String listCartCookieStr);


}
