package com.zxshare.gmall.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.bean.PmsProductImage;
import com.zxshare.gmall.bean.PmsProductInfo;
import com.zxshare.gmall.bean.PmsProductSaleAttr;
import com.zxshare.gmall.bean.PmsProductSaleAttrValue;
import com.zxshare.gmall.mapper.PmsProductImageMapper;
import com.zxshare.gmall.mapper.PmsProductInfoMapper;
import com.zxshare.gmall.mapper.PmsProductSaleAttrMapper;
import com.zxshare.gmall.mapper.PmsProductSaleAttrValueMapper;
import com.zxshare.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;


    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override

    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        //PmsProductInfo
        pmsProductInfoMapper.insert(pmsProductInfo);

        //PmsProductSaleAttrList
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {

            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);

            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();

            //PmsProductSaleAttrValueList
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {

                //TODO isChecked 没有设置
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());

                pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
            }
        }


        //PmsProductImageList
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insert(pmsProductImage);
        }
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        return pmsProductImageMapper.select(pmsProductImage);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        //PmsProductSaleAttrList
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> select = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        //PmsProductSaleAttrValueList
        for (PmsProductSaleAttr productSaleAttr : select) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> select1 = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(select1);
        }
        return select;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {

//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(productId);
//
//        List<PmsProductSaleAttr> select = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
//
//        for (PmsProductSaleAttr productSaleAttr : select) {
//            String saleAttrId = productSaleAttr.getSaleAttrId();
//
//
//            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
//            pmsProductSaleAttrValue.setProductId(productId);
//            List<PmsProductSaleAttrValue> select1 = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//            productSaleAttr.setSpuSaleAttrValueList(select1);
//


        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrs;
    }
}
