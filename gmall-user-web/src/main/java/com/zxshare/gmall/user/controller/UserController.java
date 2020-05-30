package com.zxshare.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxshare.gmall.bean.UmsMember;
import com.zxshare.gmall.bean.UmsMemberReceiveAddress;
import com.zxshare.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        List<UmsMemberReceiveAddress> umsMembers = userService.getReceiveAddressByMemberId(memberId);
        return umsMembers;

    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "hello wof";
    }
}
