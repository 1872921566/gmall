package com.zxshare.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zxshare.gmall.bean.PmsProductSaleAttr;
import com.zxshare.gmall.bean.PmsSkuInfo;
import com.zxshare.gmall.bean.PmsSkuSaleAttrValue;
import com.zxshare.gmall.service.SkuService;
import com.zxshare.gmall.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;


@Controller
@CrossOrigin
@Slf4j
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;


    @RequestMapping(value = "/{skuId}.html")
    public String getSkuById(@PathVariable(value = "skuId")String skuId, ModelMap modelMap){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);


        //sku对象
        modelMap.put("skuInfo",pmsSkuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> attrList = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku",attrList);

        //查询当前sku所在spu所有的sku集合的hash表
        List<PmsSkuInfo> skuInfos = skuService.getSpuAttrValueListBySpu(pmsSkuInfo.getProductId());

        //hash
        HashMap<String, String> skuSaleAttrHash = new HashMap<>(16);
        for (PmsSkuInfo skuInfo : skuInfos) {
            String k="";

            String v=skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k+=pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }

        String skuSaleAttrJSON = JSON.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrJSON",skuSaleAttrJSON);
        return "item";

    }


}

