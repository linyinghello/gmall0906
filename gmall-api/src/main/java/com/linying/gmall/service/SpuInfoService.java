package com.linying.gmall.service;

import com.linying.gmall.bean.SkuInfo;
import com.linying.gmall.bean.SpuInfo;

import java.util.List;

public interface SpuInfoService {
    List<SpuInfo> getSpuInfoList(String catalogSpu3Id);
    void  saveAllInformation(SpuInfo spuInfo);



}
