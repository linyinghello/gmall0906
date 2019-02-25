package com.linying.gmall.mamage.Service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.SpuSaleAttrValue;
import com.linying.gmall.mamage.mapper.SpuSaleAttrValueMapper;
import com.linying.gmall.service.SpuSaleAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuSaleAttrValueServiceImpl implements SpuSaleAttrValueService {

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<SpuSaleAttrValue> getSaleAttrService(String spuId) {
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuId);
        List<SpuSaleAttrValue> spuSaleAttrValues =  spuSaleAttrValueMapper.select(spuSaleAttrValue);
        return spuSaleAttrValues;
    }
}
