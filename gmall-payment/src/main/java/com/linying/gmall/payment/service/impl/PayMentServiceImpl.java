package com.linying.gmall.payment.service.impl;



import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.linying.gmall.bean.PaymentInfo;
import com.linying.gmall.mamage.util.ActiveMQUtil;
import com.linying.gmall.payment.mapper.PaymentInfoMapper;
import com.linying.gmall.service.PayMentService;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayMentServiceImpl implements PayMentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    AlipayClient alipayClient;
    
    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public void save(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatePayment(PaymentInfo paymentInfo) {
        Example example = new Example(PaymentInfo.class);
        example.createCriteria().andEqualTo("outTradeNo",paymentInfo.getOutTradeNo());
        paymentInfoMapper.updateByExampleSelective(paymentInfo,example);
    }

    @Override
    public void sendPaymentResult(String out_trade_no,String result,String trade_no) {

        Connection connection =activeMQUtil.getConnection();
        try {

            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue testqueue = session.createQueue("PAYMENT_SUCCESS_QUEUE");

            MessageProducer producer = session.createProducer(testqueue);
            MapMessage mapMessage=new ActiveMQMapMessage();
            mapMessage.setString("out_trade_no",out_trade_no);
            mapMessage.setString("result",result);
            mapMessage.setString("tickingNo",trade_no);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(mapMessage);
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void payDelayQueue(String outTradeNo, int i) {


        Connection connection =activeMQUtil.getConnection();
        try {

            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue testqueue = session.createQueue("PAYMENT_CHECK_QUEUE");

            MessageProducer producer = session.createProducer(testqueue);
            MapMessage mapMessage=new ActiveMQMapMessage();
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*15);
            mapMessage.setString("out_trade_no",outTradeNo);
            mapMessage.setInt("count",i);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(mapMessage);
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaymentInfo delayCheck(String outTradeNo, int count) {

        PaymentInfo paymentInfo = new PaymentInfo();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outTradeNo);
        String s = JSON.toJSONString(map);
        request.setBizContent(s);

        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        if(response.isSuccess()){

            paymentInfo.setCallbackTime(new Date());
            paymentInfo.setPaymentStatus(response.getTradeStatus());
            paymentInfo.setOutTradeNo(outTradeNo);
            paymentInfo.setCallbackContent(response.getMsg());
            paymentInfo.setAlipayTradeNo(response.getTradeNo());
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return paymentInfo;
    }

    @Override
    public boolean checkPaymentStatus(String outTradeNo) {

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo1 = paymentInfoMapper.selectOne(paymentInfo);
        if(paymentInfo1.getPaymentStatus().equals("已支付")){
            return true;
        }

        return false;
    }
}
