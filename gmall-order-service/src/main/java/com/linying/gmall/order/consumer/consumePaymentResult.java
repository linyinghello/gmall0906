package com.linying.gmall.order.consumer;

import com.linying.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class consumePaymentResult {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentResults(MapMessage mapMessage)throws JMSException{
        String outTradeNo = mapMessage.getString("out_trade_no");
        String result = mapMessage.getString("result");
        String trackingNo = mapMessage.getString("tickingNo");
        System.out.println(result);

        orderService.updateOrder(outTradeNo,result,trackingNo);
        orderService.sendOrderResult(outTradeNo);

    }
}
