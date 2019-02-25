package com.linying.gmall.service;

import com.linying.gmall.bean.BaseSaleAttr;

import java.util.List;

public interface SaleAttrService {
    public List<BaseSaleAttr> getSaleAttrList();

    List<BaseSaleAttr> getSaleAttrService(String spuId);
}
