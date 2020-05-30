package com.zxshare.gmall.interceptors;

import com.zxshare.gmall.anotations.LoginRequired;
import com.zxshare.gmall.util.CookieUtil;
import com.zxshare.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截代码
        HandlerMethod hMthod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hMthod.getMethodAnnotation(LoginRequired.class);

        //是否拦截
        if (methodAnnotation == null) {
            System.out.println("请求地址："+request.getRequestURI());
            System.out.println("未拦截..............");
            return true;
        }

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        String newToken = request.getParameter("token");

        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }

        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }

        boolean loginSuccess = methodAnnotation.loginSuccess();

        String success = "false";
        //验证
        if (StringUtils.isNotBlank(token)){
             success = HttpclientUtil.doGet("Http://127.0.0.1:8085/verify?token=" + token);
        }

        //拦截后是否必须登录才能访问
        if (loginSuccess) {
            //必须登录成功才能访问
            if (!"success".equals(success)) {
                //回认证中心
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("Http://127.0.0.1:8085/index?ReturnUrl="+requestURL);
                return false;
            }

            //覆盖Cookie
            request.setAttribute("memberId", "");
            request.setAttribute("nickName", "");


        } else {
            //验证
            if ("success".equals(success)) {
                request.setAttribute("memberId", "");
                request.setAttribute("nickName", "");

            }

        }
        if (StringUtils.isNotBlank(token)){
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);

        }

        return true;
    }
}
