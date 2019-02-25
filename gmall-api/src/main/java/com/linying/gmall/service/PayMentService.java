package com.linying.gmall.service;

import com.linying.gmall.bean.PaymentInfo;

public interface PayMentService {

    void save(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);
    void sendPaymentResult(String out_trade_no,String result,String trade_no);

    void payDelayQueue(String outTradeNo, int i);

    PaymentInfo delayCheck(String outTradeNo, int count);

    boolean checkPaymentStatus(String outTradeNo);
}
