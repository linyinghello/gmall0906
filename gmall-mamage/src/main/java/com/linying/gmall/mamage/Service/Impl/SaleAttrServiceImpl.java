package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseSaleAttr;
import com.linying.gmall.mamage.mapper.SaleAttrMapper;
import com.linying.gmall.service.SaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SaleAttrServiceImpl implements SaleAttrService {

    @Autowired
    private SaleAttrMapper saleAttrMapper;
    @Override
    public List<BaseSaleAttr> getSaleAttrList() {
        return saleAttrMapper.selectAll();
    }

    @Override
    public List<BaseSaleAttr> getSaleAttrService(String spuId) {
        return null;
    }
}
