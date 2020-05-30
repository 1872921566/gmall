package com.zxshare.gmall.service;

import com.zxshare.gmall.bean.PmsSearchSkuInfo;
import com.zxshare.gmall.param.PmsSearchParam;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) throws IOException;
}
