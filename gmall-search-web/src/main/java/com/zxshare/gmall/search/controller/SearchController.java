package com.zxshare.gmall.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zxshare.gmall.bean.PmsBaseAttrInfo;
import com.zxshare.gmall.bean.PmsBaseAttrValue;
import com.zxshare.gmall.bean.PmsSearchSkuInfo;
import com.zxshare.gmall.bean.PmsSkuAttrValue;
import com.zxshare.gmall.param.PmsSearchCrumb;
import com.zxshare.gmall.param.PmsSearchParam;
import com.zxshare.gmall.service.AttrService;
import com.zxshare.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;

@Controller
public class SearchController {

    @Reference
    AttrService attrService;

    @Reference
    SearchService searchSevice;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) throws IOException {

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchSevice.list(pmsSearchParam);
//        if (pmsSearchSkuInfoList==null){
//            return "error";
//        }
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);

        //抽取检索结果所包含的平台属性集合-利用set集合无序，不可重复的特点去重
        Set<String> valueIdSet = new HashSet<>();

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                valueIdSet.add(pmsSkuAttrValue.getValueId());

            }
        }

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        //        //删除查询的valueid本身的属性
        String[] DelValue = pmsSearchParam.getValueId();


        if (DelValue != null) {

            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();

            for (String delId : DelValue) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                String urlParamForCrumb = getUrlParamForCrumb(pmsSearchParam, delId);
                pmsSearchCrumb.setValueId(delId);
                pmsSearchCrumb.setUrlParam(urlParamForCrumb);
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo next = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = next.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();

                        if (valueId.equals(delId)) {
                            String valueName = pmsBaseAttrValue.getValueName();
                            pmsSearchCrumb.setValueName(valueName);
                            pmsSearchCrumbs.add(pmsSearchCrumb);
                            iterator.remove();
                        }
                    }
                }
            }
            modelMap.put("attrValueSelectedList",pmsSearchCrumbs);
        }


        modelMap.put("attrList", pmsBaseAttrInfos);

        //ParamStr 请求url

        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);

        //面包屑
//        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
//        if(DelValue!=null){
//            for (String Id : DelValue) {
//                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
//                pmsSearchCrumb.setValueId(Id);
//                pmsSearchCrumb.setUrlParam();
//                pmsSearchCrumb.setValueName();
//
//                pmsSearchCrumbs.add(pmsSearchCrumb);
//            }
//        }


//        modelMap.put("attrValueSelectedList",pmsSearchCrumbs);
        return "list";
    }


    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "&catalog3Id=" + keyword;
        }


        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "&catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {

                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;

            }
        }
        return urlParam;
    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValue) {
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "&catalog3Id=" + keyword;
        }


        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "&catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                if (!pmsSkuAttrValue.equals(delValue)) {
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }


            }
        }

        return urlParam;
    }


    @RequestMapping("index")
    public String index() {

        return "index";
    }
}
