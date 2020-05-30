package com.zxshare.gmall.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
@Slf4j
public class SpuController {

    @Reference
    SpuService spuService;


    //http://127.0.0.1:8082/spuSaleAttrList?spuId=24
    @RequestMapping(value = "/spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam String spuId) {
        return spuService.spuSaleAttrList(spuId);
    }

    //http://127.0.0.1:8082/spuImageList?spuId=24
    @RequestMapping(value = "/spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(@RequestParam String spuId) {
        return spuService.spuImageList(spuId);
    }


    //http://127.0.0.1:8082/saveSpuInfo
    @RequestMapping(value = "/saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    //http://127.0.0.1:8082/fileUpload
    @RequestMapping(value = "/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam MultipartFile file) {
        //分布式文件系统上传文件，并返回一个服务器存放路径放回给前端；
        String imgUrl = PmsUploadFileUtil.fileUpload(file);
        return imgUrl;
    }

    //http://127.0.0.1:8082/spuList?catalog3Id=101
    @RequestMapping(value = "/spuList", method = RequestMethod.GET)
    @ResponseBody
    public List<PmsProductInfo> spuList(@RequestParam String catalog3Id) {
        return spuService.spuList(catalog3Id);
    }
}
