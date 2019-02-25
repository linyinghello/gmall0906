package com.linying.gmall.service;

import com.linying.gmall.bean.SpuSaleAttr;

import java.util.List;

public interface SpuSaleAttrService {

    List<SpuSaleAttr> getSaleAttrService(String spuId);

    List<SpuSaleAttr>  getSaleAttrListBySpuId(String spuId,String skuId);
}
