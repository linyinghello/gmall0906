package com.linying.gmall.payment.consumerLintener;


import com.linying.gmall.bean.PaymentInfo;
import com.linying.gmall.service.PayMentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Objects;

@Component
public class DelayCheckListener {

    @Autowired
    PayMentService payMentService;

    @JmsListener(destination = "PAYMENT_CHECK_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentResults(MapMessage mapMessage)throws JMSException {
        String outTradeNo = mapMessage.getString("out_trade_no");
        int count = mapMessage.getInt("count");

        if(count>0){
            PaymentInfo paymentInfo = payMentService.delayCheck(outTradeNo,count);
            System.out.println("第"+(6-count)+"次检查");
            count--;
            if(Objects.equals(paymentInfo.getPaymentStatus(),"TRADE_SUCCESS")||Objects.equals(paymentInfo.getPaymentStatus(),"TRADE_FINISHED")){
                System.out.println("支付成功");
                boolean b= payMentService.checkPaymentStatus(outTradeNo);
                if(!b){
                    // 修改支付信息
                    paymentInfo.setPaymentStatus("已支付");
                    paymentInfo.setCallbackContent(paymentInfo.getCallbackContent());
                    paymentInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
                    paymentInfo.setAlipayTradeNo(paymentInfo.getAlipayTradeNo());
                    paymentInfo.setCallbackTime(new Date());

                    payMentService.updatePayment(paymentInfo);

                    String result ="已支付";
                    // 发送系统消息，出发并发商品支付业务服务O2O消息队列M
                    payMentService.sendPaymentResult(outTradeNo,result,paymentInfo.getAlipayTradeNo());
                    return;
                } else{
                    return;
                }

            }else{
                System.out.println("支付失败，进行下一次检查");
                payMentService.payDelayQueue(outTradeNo,count);
            }
        }else {
                System.out.println("支付失败");
        }





    }


}
