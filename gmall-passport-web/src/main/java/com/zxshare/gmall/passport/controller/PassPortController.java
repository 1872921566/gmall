package com.zxshare.gmall.passport.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zxshare.gmall.bean.UmsMember;
import com.zxshare.gmall.service.UserService;
import com.zxshare.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sir
 */
@Controller
public class PassPortController {

    @Reference
    UserService userService;

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token) {
        System.out.println("Function:verify()......");

        return "tokenTest";
    }

    @RequestMapping("/vlogin")
    @ResponseBody
    public String vlogin(String code) {


        return "";
    }


    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request) {

        String token = "";
        UmsMember umsMemberLogin = userService.login(umsMember);

        if (umsMemberLogin != null) {
            //登录成功

            //制作token

            Map<String, Object> userMap = new HashMap<>(16);
            String nickname = umsMemberLogin.getNickname();
            String id = umsMemberLogin.getId();
            userMap.put("nickname", nickname);
            userMap.put("memberId", id);

            String ip = request.getHeader("X-Real-IP");
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
                if (StringUtils.isBlank(ip)) {
                    ip = "127.0.0.1";
                }
            }

            token = JwtUtil.encode("2019gmall0105", userMap, ip);
            userService.addUserToken(token,id);

        } else {
            //登录失败

            token = "fail";

        }
        return token;
    }


    @RequestMapping("/index")
    public String index(String ReturnUrl, ModelMap modelMap) {

        modelMap.put("ReturnUrl", ReturnUrl);
        return "index";
    }
}
