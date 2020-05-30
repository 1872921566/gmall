package com.zxshare.gmall.service;

import com.zxshare.gmall.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSpuAttrValueListBySpu(String productId);

    List<PmsSkuInfo> getAllSku();
}
