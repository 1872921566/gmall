package com.zxshare.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.bean.PmsSkuAttrValue;
import com.zxshare.gmall.bean.PmsSkuImage;
import com.zxshare.gmall.bean.PmsSkuInfo;
import com.zxshare.gmall.bean.PmsSkuSaleAttrValue;
import com.zxshare.gmall.mapper.PmsSkuAttrValueMapper;
import com.zxshare.gmall.mapper.PmsSkuImageMapper;
import com.zxshare.gmall.mapper.PmsSkuInfoMapper;
import com.zxshare.gmall.mapper.PmsSkuSaleAttrValueMapper;
import com.zxshare.gmall.service.SkuService;
import com.zxshare.gmall.service.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //PmsSkuInfoMapper

        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        //PmsSkuImageMapper
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {

            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        //pmsSkuSaleAttrValueMapper
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        //pmsSkuAttrValueMapper
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        return "success";
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        //param
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);

        //mapper
        List<PmsSkuImage> select = pmsSkuImageMapper.select(pmsSkuImage);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        //return skuInfo
        skuInfo.setSkuImageList(select);
        return skuInfo;
    }



    @Override
    public List<PmsSkuInfo> getSpuAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSpuAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String SkuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(SkuId);

            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }
}
