package com.zxshare.gmall.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;


    @RequestMapping(value = "/baseSaleAttrList", method = RequestMethod.POST)
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return attrService.baseSaleAttrList();
    }


    @RequestMapping(value = "/getAttrValueList", method = RequestMethod.POST)
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam String attrId) {
        return attrService.getAttrValueList(attrId);
    }


    @RequestMapping(value = "/saveAttrInfo", method = RequestMethod.POST)
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {


        String key = attrService.saveAttrInfo(pmsBaseAttrInfo);


        return key;
    }

    @RequestMapping(value = "attrInfoList", method = RequestMethod.GET)
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(@RequestParam String catalog3Id) {
        return attrService.attrInfoList(catalog3Id);
    }

}
