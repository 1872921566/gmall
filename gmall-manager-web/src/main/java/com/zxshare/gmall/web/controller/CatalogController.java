package com.zxshare.gmall.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping(value = "/getCatalog3",method = RequestMethod.POST)
    @ResponseBody
    public List<PmsBaseCatalog3>getCatalog3(@RequestParam String catalog2Id){
        List<PmsBaseCatalog3> catalog3List=catalogService.getCatalog3(catalog2Id);
        return catalog3List;
    }


    @RequestMapping(value = "/getCatalog2",method = RequestMethod.POST)
    @ResponseBody
    public List<PmsBaseCatalog2>getCatalog2(@RequestParam String catalog1Id){
        List<PmsBaseCatalog2> catalog1List=catalogService.getCatalog2(catalog1Id);
        return catalog1List;
    }

    @RequestMapping(value = "/getCatalog1",method = RequestMethod.POST)
    @ResponseBody
    public List<PmsBaseCatalog1>getCatalog1(){

        return  catalogService.getCatalog1();
    }
}
