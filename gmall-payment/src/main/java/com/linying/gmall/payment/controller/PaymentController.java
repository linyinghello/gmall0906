package com.linying.gmall.payment.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.linying.gmall.annotations.LoginRequired;
import com.linying.gmall.bean.CartInfo;
import com.linying.gmall.bean.OrderInfo;
import com.linying.gmall.bean.PaymentInfo;
import com.linying.gmall.mamage.util.ActiveMQUtil;
import com.linying.gmall.payment.config.AlipayConfig;
import com.linying.gmall.service.CartService;
import com.linying.gmall.service.OrderService;
import com.linying.gmall.service.PayMentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @Reference
    OrderService orderService;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Autowired
    PayMentService payMentService;

    @RequestMapping("alipay/callback/return")
    public String callBackReturn(HttpServletRequest request,Map<String,String> paramsMap){


        String out_trade_no = request.getParameter("out_trade_no");
        String trade_no = request.getParameter("trade_no");
        System.out.println(trade_no);
        String sign = request.getParameter("sign");

        try {
            boolean b = AlipaySignature.rsaCheckV1(paramsMap,AlipayConfig.alipay_public_key,AlipayConfig.charset,AlipayConfig.sign_type);// 对支付宝回调签名的校验
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        boolean b= payMentService.checkPaymentStatus(out_trade_no);
        if(!b){
            // 修改支付信息
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setCallbackContent(request.getQueryString());
            paymentInfo.setOutTradeNo(out_trade_no);
            paymentInfo.setAlipayTradeNo(trade_no);
            paymentInfo.setCallbackTime(new Date());

            payMentService.updatePayment(paymentInfo);

            String result ="已支付";
            // 发送系统消息，出发并发商品支付业务服务O2O消息队列M
            payMentService.sendPaymentResult(out_trade_no,result,trade_no);
        }

        return "finish";
    }

    @RequestMapping("payMentIndex")
    public String toPayMentIndex(Model model,BigDecimal totalAmount,String outTradeNo){

        model.addAttribute("orderId",outTradeNo);
        model.addAttribute("totalAmount",totalAmount);

        return "paymentindex";
    }

    @LoginRequired(isNeedLogin = true)
    @RequestMapping("alipay/submit")
    @ResponseBody
    public String goToPay(HttpServletRequest request, HttpServletResponse response,String outTradeNo,BigDecimal totalAmount){

        OrderInfo orderInfo = orderService.getCartInfoByOutTradeNo(outTradeNo);
        String skuName = orderInfo.getOrderDetailList().get(0).getSkuName();



        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);//在公共参数中设置回跳和通知地址
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("out_trade_no",outTradeNo);
        requestMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        requestMap.put("total_amount","0.01");
        requestMap.put("subject",skuName);
        alipayRequest.setBizContent(JSON.toJSONString(requestMap));//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOutTradeNo(outTradeNo);
        paymentInfo.setOrderId(orderInfo.getOrderDetailList().get(0).getOrderId());
        paymentInfo.setTotalAmount(totalAmount);
        paymentInfo.setPaymentStatus("未支付");
        paymentInfo.setSubject(skuName);
        paymentInfo.setCreateTime(new Date());
        payMentService.save(paymentInfo);


        payMentService.payDelayQueue(outTradeNo,8);


      return form;
    }


}
