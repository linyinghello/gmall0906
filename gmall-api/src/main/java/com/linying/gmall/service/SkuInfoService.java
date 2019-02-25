package com.linying.gmall.service;

import com.linying.gmall.bean.SkuInfo;

import java.util.List;

public interface SkuInfoService {
    void saveSkuInfo(SkuInfo skuInfo);
    List<SkuInfo> getSpuInfoService(String spuId);
    SkuInfo getSkuBySkuId(String skuId);

    List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId);

    SkuInfo item(String skuId);
    List<SkuInfo> SkuListByCatalog3Id(String catalog3Id);
    void updateEditSkuListJson(SkuInfo skuInfo);
}
