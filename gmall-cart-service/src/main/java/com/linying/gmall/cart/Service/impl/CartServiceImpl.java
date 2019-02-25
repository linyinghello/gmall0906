package com.linying.gmall.cart.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.CartInfo;
import com.linying.gmall.cart.mapper.CartMapper;


import com.linying.gmall.mamage.util.RedisUtil;
import com.linying.gmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    RedisUtil redisUtil;




    @Override
    public CartInfo ifExists(String userId, String skuId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        cartInfo.setSkuId(skuId);
        CartInfo cartInfo1 = cartMapper.selectOne(cartInfo);

        return cartInfo1;
    }

    @Override
    public void updateCartInfo(CartInfo cartInfoByUserIdAndSkuId) {
        cartMapper.updateByPrimaryKeySelective(cartInfoByUserIdAndSkuId);
    }

    @Override
    public void setCartInfo(CartInfo cartInfo) {
        cartMapper.insert(cartInfo);
    }

    @Override
    public List<CartInfo> getCartInfosByUserId(String userId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartInfos = cartMapper.select(cartInfo);
        return cartInfos;
    }

    @Override
    public void updateCartInfoChecked(CartInfo cartInfo) {
       String userId = cartInfo.getUserId();
       String skuId = cartInfo.getSkuId();
       CartInfo cartInfo2 = new CartInfo();
       cartInfo2.setUserId(userId);
       cartInfo2.setSkuId(skuId);
        CartInfo cartInfo1 = cartMapper.selectOne(cartInfo2);
        String id = cartInfo1.getId();
        cartInfo.setId(id);
        cartMapper.updateByPrimaryKeySelective(cartInfo);
    }

    @Override
    public void decNumAndCartprice(String skuId, String userId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuId);
        cartInfo.setUserId(userId);
        CartInfo cartInfo1 = cartMapper.selectOne(cartInfo);
        cartInfo1.setSkuNum(cartInfo1.getSkuNum()-1);
        cartInfo1.setCartPrice(cartInfo1.getSkuPrice().multiply(new BigDecimal(cartInfo1.getSkuNum())));
        cartMapper.updateByPrimaryKeySelective(cartInfo1);
    }

    @Override
    public void addNumAndCartprice(String skuId, String userId) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuId);
        cartInfo.setUserId(userId);
        CartInfo cartInfo1 = cartMapper.selectOne(cartInfo);
        cartInfo1.setSkuNum(cartInfo1.getSkuNum()+1);
        cartInfo1.setCartPrice(cartInfo1.getSkuPrice().multiply(new BigDecimal(cartInfo1.getSkuNum())));
        cartMapper.updateByPrimaryKeySelective(cartInfo1);
    }

    @Override
    public void flushCartCacheByUser(String userId){
        List<CartInfo> cartInfos = getCartInfosByUserId(userId);
        Jedis jedis = redisUtil.getJedis();
        if(cartInfos !=null){
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            for (CartInfo cartInfo : cartInfos) {
                stringStringHashMap.put(cartInfo.getId(), JSON.toJSONString(cartInfo));
            }
            jedis.hmset("cart:"+userId+":info",stringStringHashMap);
        }
        jedis.close();
    }

    @Override
    public List<CartInfo> cartListFromCache(String userId){
        List<CartInfo> cartInfos = new ArrayList<>();
        Jedis jedis = redisUtil.getJedis();
        List<String> hvals = jedis.hvals("cart:" + userId + ":info");
        if(hvals==null){
             cartInfos = getCartInfosByUserId(userId);
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            for (CartInfo cartInfo : cartInfos) {
                stringStringHashMap.put(cartInfo.getId(), JSON.toJSONString(cartInfo));
            }
            jedis.hmset("cart:"+userId+":info",stringStringHashMap);
            return cartInfos;
        }
        for (String hval : hvals) {
            CartInfo cartInfo = JSON.parseObject(hval, CartInfo.class);
            cartInfos.add(cartInfo);
        }
        return cartInfos;
    }

    @Override
    public void updateCookieAndDb(String userId, String listCartCookieStr) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartInfoByDb = cartMapper.select(cartInfo);

        Map<String,CartInfo> mapFromDb = new HashMap<>();

        for (CartInfo info : cartInfoByDb) {
            mapFromDb.put(info.getSkuId(),info);
        }





        if(listCartCookieStr!=null) {
            Map<String,CartInfo> mapFromCookie = new HashMap<>();
            List<CartInfo> cartInfosByCookie = JSON.parseArray(listCartCookieStr, CartInfo.class);
            for (CartInfo info : cartInfosByCookie) {
                mapFromCookie.put(info.getSkuId(), info);
            }

            for(String skuId :mapFromDb.keySet()){
                CartInfo cartInfoFromDb = mapFromDb.get(skuId);

                CartInfo cartIndoFromCookie = mapFromCookie.get(skuId);

                if(cartIndoFromCookie!=null){
                    cartInfoFromDb.setSkuNum(cartInfoFromDb.getSkuNum()+cartIndoFromCookie.getSkuNum());
                    cartInfoFromDb.setCartPrice(cartInfoFromDb.getCartPrice().multiply(new BigDecimal(cartInfoFromDb.getSkuNum())));
                    cartMapper.updateByPrimaryKeySelective(cartInfoFromDb);
                    mapFromCookie.remove(skuId);
                }

            }

            for(String skuId :mapFromCookie.keySet()){
                CartInfo cartInfoFromDb = mapFromCookie.get(skuId);
                cartInfoFromDb.setUserId(userId);
                cartMapper.insert(cartInfoFromDb);
            }

        }



    }


}
