package com.linying.gmall.service;

import com.linying.gmall.bean.CartInfo;
import com.linying.gmall.bean.OrderInfo;

public interface OrderService {

   void genTradeCode(String tradeCode,String userId);

    boolean checkTradeCode(String tradeCode,String userId);

    void saveOrder(OrderInfo orderInfo);

    OrderInfo getCartInfoByOutTradeNo(String outTradeNo);

    void updateOrder(String outTradeNo, String result, String trackingNo);

    void sendOrderResult(String outTradeNo);
}
