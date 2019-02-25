package com.linying.gmall.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.CartInfo;
import com.linying.gmall.bean.SkuInfo;
import com.linying.gmall.annotations.LoginRequired;
import com.linying.gmall.service.CartService;
import com.linying.gmall.service.SkuInfoService;
import com.linying.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Reference
    private CartService cartService;

    @Reference
    private SkuInfoService skuInfoService;


    @LoginRequired(isNeedLogin = false)
    @RequestMapping("addToCart")
    public String addToCart(HttpServletRequest request, HttpServletResponse response, String skuId, int num, Model model, HttpSession session) {

        String userId = (String)request.getAttribute("userId");
        SkuInfo skuInfo = skuInfoService.getSkuBySkuId(skuId);
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuId);
        cartInfo.setSkuPrice(skuInfo.getPrice());
        cartInfo.setCartPrice(skuInfo.getPrice().multiply(new BigDecimal(num)));
        cartInfo.setSkuNum(num);
        cartInfo.setUserId(userId);
        cartInfo.setIsChecked("1");
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());


        List<CartInfo> cartInfos = new ArrayList<>();

        if (StringUtils.isBlank(userId)) {
            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);

            if (StringUtils.isBlank(listCartCookieStr)) {
                cartInfos = new ArrayList<>();
                cartInfos.add(cartInfo);
            } else {
                boolean b = if_new_cart(cartInfos, cartInfo);
                if (b) {
                    cartInfos.add(cartInfo);
                } else {
                    for (CartInfo info : cartInfos) {
                        if (info.getSkuId().equals(skuId)) {
                            info.setSkuNum(info.getSkuNum() + num);
                            System.err.println(info.getSkuNum());
                            info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        }
                    }
                }

            }
            CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
        } else {

            CartInfo cartInfoByUserIdAndSkuId = cartService.ifExists(userId, skuId);
            if (cartInfoByUserIdAndSkuId != null) {
                cartInfoByUserIdAndSkuId.setSkuNum(cartInfoByUserIdAndSkuId.getSkuNum() + num);
                cartInfoByUserIdAndSkuId.setCartPrice(cartInfoByUserIdAndSkuId.getSkuPrice().multiply(new BigDecimal(cartInfoByUserIdAndSkuId.getSkuNum())));
                cartService.updateCartInfo(cartInfoByUserIdAndSkuId);
            } else {

                cartService.setCartInfo(cartInfo);
            }
        }

        for (CartInfo info : cartInfos) {
            if (info.getSkuId().equals(skuId)) {
                cartInfo = info;
            }
        }
        session.setAttribute("cartInfo", cartInfo);


        return "redirect:success";
    }

    private boolean if_new_cart(List<CartInfo> cartInfos, CartInfo cartInfo) {
        boolean b = true;

        for (CartInfo info : cartInfos) {
            String skuId = info.getSkuId();
            if (skuId.equals(cartInfo.getSkuId())) {
                b = false;
            }
        }
        return b;
    }

    @RequestMapping("success")
    public String toCartList() {

        return "success";
    }


    @LoginRequired(isNeedLogin = false)
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, Model model) {

        BigDecimal totalPrice = new BigDecimal("0");

        List<CartInfo> cartInfos = new ArrayList<>();
        String userId = (String)request.getAttribute("userId");

        if (StringUtils.isBlank(userId)) {

            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);

            for (CartInfo cartInfo : cartInfos) {
                totalPrice = totalPrice.add(cartInfo.getCartPrice());
            }
        } else {
            cartInfos = cartService.getCartInfosByUserId(userId);
            for (CartInfo cartInfo : cartInfos) {

                totalPrice = totalPrice.add(cartInfo.getCartPrice());
            }
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartList", cartInfos);
        return "cartList";
    }

    @LoginRequired(isNeedLogin = false)
    @RequestMapping("checkCart")
    @ResponseBody
    public BigDecimal checkCart(HttpServletRequest request, Model model, CartInfo cartInfo,HttpServletResponse response) {

        String userId = (String)request.getAttribute("userId");
        BigDecimal totalPrice = new BigDecimal("0");
        cartInfo.setUserId(userId);
        String skuId = cartInfo.getSkuId();
        System.out.println(skuId);
        String isChecked = cartInfo.getIsChecked();
        System.out.println(isChecked);
        List<CartInfo> cartInfos = new ArrayList<>();


        if (StringUtils.isBlank(userId)) {

            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);

            for (CartInfo info : cartInfos) {
                if (info.getSkuId().equals(skuId)) {
                    if (isChecked.equals("1")) {
                        totalPrice = totalPrice.add(info.getCartPrice());
                        info.setIsChecked("1");
                        CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
                    }
                } else {
                    totalPrice.add(info.getCartPrice());
                }


            }

        } else {

            cartService.updateCartInfoChecked(cartInfo);

            cartInfos = cartService.getCartInfosByUserId(userId);


            for (CartInfo info : cartInfos) {

               if(info.getIsChecked().equals("1")){
                   totalPrice = totalPrice.add(info.getCartPrice());
                }


            }


        }

        System.out.println(totalPrice);
        return totalPrice;
    }



    @LoginRequired(isNeedLogin = false)
    @RequestMapping("cartSelectAll")
    @ResponseBody
    public BigDecimal cartSelectAll(HttpServletRequest request,Model mode,HttpServletResponse response){
        BigDecimal totalPrice = new BigDecimal("0");
        String userId = (String)request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);
            for (CartInfo cartInfo : cartInfos) {
                totalPrice = totalPrice.add(cartInfo.getCartPrice());
                cartInfo.setIsChecked("1");
                CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
            }
        } else {
            cartInfos = cartService.getCartInfosByUserId(userId);
            for (CartInfo cartInfo : cartInfos) {
                cartInfo.setIsChecked("1");
                cartService.updateCartInfoChecked(cartInfo);
                totalPrice = totalPrice.add(cartInfo.getCartPrice());
            }
        }
        return totalPrice;

    }


    @LoginRequired(isNeedLogin = false)
    @RequestMapping("cartSelectNull")
    @ResponseBody
    public BigDecimal cartSelectNull(HttpServletRequest request,Model mode,HttpServletResponse response){
        BigDecimal totalPrice = new BigDecimal("0");
        String userId = (String)request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);
            for (CartInfo cartInfo : cartInfos) {
               cartInfo.setIsChecked("0");

            }
            CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
        } else {
            cartInfos = cartService.getCartInfosByUserId(userId);
            for (CartInfo cartInfo : cartInfos) {
                cartInfo.setIsChecked("0");
                cartService.updateCartInfoChecked(cartInfo);
                totalPrice = totalPrice.add(cartInfo.getCartPrice());
            }
        }
        System.out.println(totalPrice);
        return totalPrice;
    }

    @LoginRequired(isNeedLogin = false)
    @RequestMapping("decOneCart")
    @ResponseBody
    public String decOneCart(CartInfo cartInfo,HttpServletRequest request,HttpServletResponse response){
        String skuId = cartInfo.getSkuId();
        String userId = (String)request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);
            for (CartInfo info : cartInfos) {
                if(info.getSkuId() == skuId){
                    info.setSkuNum(info.getSkuNum()-1);
                    info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                }

            }


            CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
        } else {
            cartService.decNumAndCartprice(skuId,userId);
        }
        return "successs";
    }

    @LoginRequired(isNeedLogin = false)
    @RequestMapping("addOneCart")
    @ResponseBody
    public String addOneCart(CartInfo cartInfo,HttpServletRequest request,HttpServletResponse response){
        String skuId = cartInfo.getSkuId();
        String userId = (String)request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            String listCartCookieStr = CookieUtil.getCookieValue(request, "listCartCookie", true);
            cartInfos = JSON.parseArray(listCartCookieStr, CartInfo.class);
            for (CartInfo info : cartInfos) {
                if(info.getSkuId() == skuId){
                    info.setSkuNum(info.getSkuNum()+1);
                    info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                }

            }


            CookieUtil.setCookie(request, response, "listCartCookie", JSON.toJSONString(cartInfos), 1000 * 66666, true);
        } else {
            cartService.addNumAndCartprice(skuId,userId);
        }
        return "successs";
    }

}
