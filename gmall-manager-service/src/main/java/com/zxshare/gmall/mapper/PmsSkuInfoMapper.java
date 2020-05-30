package com.zxshare.gmall.mapper;

import com.zxshare.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {

    List<PmsSkuInfo> selectSpuAttrValueListBySpu(@Param("productId") String productId);
}
