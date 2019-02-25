package com.linying.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.annotations.LoginRequired;
import com.linying.gmall.bean.*;
import com.linying.gmall.bean.enums.PaymentWay;
import com.linying.gmall.service.CartService;
import com.linying.gmall.service.OrderService;
import com.linying.gmall.service.SkuInfoService;
import com.linying.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@Controller
public class OrderController {

    @Reference
    UserService userService;

    @Reference
    CartService cartService;

    @Reference
    OrderService orderService;

    @Reference
    SkuInfoService skuInfoService;

    @LoginRequired(isNeedLogin = true)
    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request, HttpServletResponse response, Model model) {

        String userId = (String)request.getAttribute("userId");


        List<CartInfo> cartInfos=cartService.getCartInfosByUserId(userId);

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getIsChecked().equals("1")){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setSkuName(cartInfo.getSkuName());
                orderDetail.setImgUrl(cartInfo.getImgUrl());
                orderDetail.setSkuId(cartInfo.getSkuId());
                orderDetail.setOrderPrice(cartInfo.getCartPrice());
                orderDetail.setSkuNum(cartInfo.getSkuNum());

                orderDetail.setHasStock("1");
                orderDetailList.add(orderDetail);

            }
        }

        model.addAttribute("orderDetailList",orderDetailList);


        List<UserAddress> addressListByUserId = userService.getAddressByUserId(userId);
        model.addAttribute("userAddressList",addressListByUserId);

        BigDecimal totalAmount = new BigDecimal("0");

        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getIsChecked().equals("1")){
               totalAmount = totalAmount.add(cartInfo.getCartPrice());
            }
        }

        model.addAttribute("totalAmount",totalAmount);

        String tradeCode = UUID.randomUUID().toString();
        model.addAttribute("tradeCode",tradeCode);

        orderService.genTradeCode(tradeCode,userId);


        return "trade";
    }

    @LoginRequired(isNeedLogin = true)
    @RequestMapping("submitOrder")
    public String submitOrder(String deliveryAddressId,String tradeCode,HttpServletResponse response, HttpServletRequest request,Model model ){
        String  userId = (String)request.getAttribute("userId");
        boolean b = orderService.checkTradeCode(tradeCode,userId);
        if(b){
            UserAddress userAddress = userService.getAddressById(deliveryAddressId);
            List<CartInfo> cartInfos = cartService.getCartInfosByUserId(userId);

            BigDecimal totalAmount = new BigDecimal("0");

            for (CartInfo cartInfo : cartInfos) {
                if(cartInfo.getIsChecked().equals("1")){
                    totalAmount = totalAmount.add(cartInfo.getCartPrice());
                }
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setProcessStatus("订单已提交");
            orderInfo.setOrderStatus("订单已提交");
            String outTradeNo = "linying"+userId;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            String format = sdf.format(date);
            outTradeNo = outTradeNo+format+System.currentTimeMillis();
            orderInfo.setOutTradeNo(outTradeNo);
            orderInfo.setUserId(userId);
            orderInfo.setPaymentWay(PaymentWay.ONLINE);
            orderInfo.setTotalAmount(totalAmount);
            orderInfo.setOrderComment("林颖商城");
            orderInfo.setDeliveryAddress(userAddress.getUserAddress());
            orderInfo.setCreateTime(new Date());
            orderInfo.setConsignee(userAddress.getConsignee());
            orderInfo.setConsigneeTel(userAddress.getPhoneNum());
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE,1);
            orderInfo.setExpireTime(c.getTime());

            List<OrderDetail> orderDetailList = new ArrayList<>();
            List<String> delCartIds = new ArrayList<>();


            for (CartInfo cartInfo : cartInfos) {
                if(cartInfo.getIsChecked().equals("1")){
                    SkuInfo skuInfo = skuInfoService.getSkuBySkuId(cartInfo.getSkuId());
                    BigDecimal reallyPrice = skuInfo.getPrice();
                    int i = reallyPrice.compareTo(cartInfo.getSkuPrice());
                    if(i==0){
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setSkuNum(cartInfo.getSkuNum());
                        orderDetail.setImgUrl(cartInfo.getImgUrl());
                        orderDetail.setOrderPrice(cartInfo.getCartPrice());
                        orderDetail.setSkuId(cartInfo.getSkuId());
                        orderDetail.setSkuName(cartInfo.getSkuName());
                        orderDetailList.add(orderDetail);
                    }else {
                        return "tradeFail";
                    }


                }
            }
            orderInfo.setOrderDetailList(orderDetailList);
            orderService.saveOrder(orderInfo);


            System.out.println(totalAmount);

            return "redirect:http://payment.gmall.com:8090/payMentIndex?outTradeNo="+outTradeNo+"&totalAmount="+totalAmount;
        }else {
            return "tradeFail";
        }
    }

}
