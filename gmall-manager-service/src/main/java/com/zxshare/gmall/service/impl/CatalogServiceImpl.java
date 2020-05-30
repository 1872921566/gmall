package com.zxshare.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.bean.PmsBaseCatalog1;
import com.zxshare.gmall.bean.PmsBaseCatalog2;
import com.zxshare.gmall.bean.PmsBaseCatalog3;
import com.zxshare.gmall.mapper.PmsBaseCatalog1Mapper;
import com.zxshare.gmall.mapper.PmsBaseCatalog2Mapper;
import com.zxshare.gmall.mapper.PmsBaseCatalog3Mapper;
import com.zxshare.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper catalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper catalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper catalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        return catalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return catalog3Mapper.select(pmsBaseCatalog3);
    }
}
