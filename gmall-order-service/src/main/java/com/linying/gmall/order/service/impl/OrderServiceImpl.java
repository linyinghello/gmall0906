package com.linying.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.CartInfo;
import com.linying.gmall.bean.OrderDetail;
import com.linying.gmall.bean.OrderInfo;
import com.linying.gmall.bean.enums.PaymentWay;
import com.linying.gmall.mamage.util.ActiveMQUtil;
import com.linying.gmall.mamage.util.RedisUtil;
import com.linying.gmall.order.mapper.OrderDetailMapper;
import com.linying.gmall.order.mapper.OrderInfoMapper;
import com.linying.gmall.service.OrderService;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Override
    public void genTradeCode(String tradeCode, String userId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+userId+":tradeCode",60*30,tradeCode);
        jedis.close();
    }

    @Override
    public boolean checkTradeCode(String tradeCode,String userId) {
        Jedis jedis = redisUtil.getJedis();
        String tradeCodeFromCache = jedis.get("user:" + userId + ":tradeCode");
        if(tradeCode.equals(tradeCodeFromCache)){

            jedis.del("user:" + userId + ":tradeCode");
            return true;
        }
        return false;
    }

    @Override
    public void saveOrder(OrderInfo orderInfo) {
        orderInfoMapper.insertSelective(orderInfo);
        String orderId = orderInfo.getId();
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();

        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderId(orderId);
            orderDetailMapper.insertSelective(orderDetail);
        }

    }

    @Override
    public OrderInfo getCartInfoByOutTradeNo(String outTradeNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOutTradeNo(outTradeNo);
       orderInfo = orderInfoMapper.selectOne(orderInfo);
        String orderId = orderInfo.getId();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);


        orderInfo.setOrderDetailList(orderDetails);
        return orderInfo;
    }

    @Override
    public void updateOrder(String outTradeNo, String result, String trackingNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setPaymentWay(PaymentWay.ONLINE);
        orderInfo.setOrderStatus("已支付");
        orderInfo.setProcessStatus("已支付");
        orderInfo.setTrackingNo(trackingNo);

        Example example = new Example(OrderInfo.class);
        example.createCriteria().andEqualTo("outTradeNo",outTradeNo);

        orderInfoMapper.updateByExampleSelective(orderInfo,example);
    }

    @Override
    public void sendOrderResult(String outTradeNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOutTradeNo(outTradeNo);
        OrderInfo order = orderInfoMapper.selectOne(orderInfo);
        
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        List<OrderDetail> select = orderDetailMapper.select(orderDetail);
        order.setOrderDetailList(select);

        Connection connection =activeMQUtil.getConnection();
        try {

            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue testqueue = session.createQueue("ORDER_SUCCESS_QUEUE");

            MessageProducer producer = session.createProducer(testqueue);
            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText(JSON.toJSONString(order));
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
