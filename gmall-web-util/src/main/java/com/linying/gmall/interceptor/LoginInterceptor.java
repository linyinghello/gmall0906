package com.linying.gmall.interceptor;

import com.linying.gmall.annotations.LoginRequired;
import com.linying.gmall.util.CookieUtil;
import com.linying.gmall.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

     HandlerMethod hm = (HandlerMethod) handler;

        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        String token = "";

        String newToken = request.getParameter("newToken");
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);

        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }

        if(StringUtils.isNotBlank((newToken))){
            token = newToken;
        }



        if(methodAnnotation != null){
            boolean loginCheck =false;
            if(StringUtils.isNotBlank(token)){
                String nip = request.getHeader("request-forwared-for");
                if(StringUtils.isBlank(nip)){
                    nip = request.getRemoteAddr();
                    if(StringUtils.isBlank(nip)){
                       // nip="0:0:0:0:0:0:0:1";
                        nip = "127.0.0.1";
                    }
                }

                String userId = HttpClientUtil.doGet("http://passport.gmall.com:8086/verify?token="+token+"&requestIp="+nip);
                if(userId!=null){
                    loginCheck = true;
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*24,true);
                    request.setAttribute("userId",userId);
                }
            }

            if(loginCheck==false && methodAnnotation.isNeedLogin()==true){
                response.sendRedirect("http://passport.gmall.com:8086/index?returnUrl="+request.getRequestURL());
                return false;
            }
        }

       ;
        return true;
    }
}
