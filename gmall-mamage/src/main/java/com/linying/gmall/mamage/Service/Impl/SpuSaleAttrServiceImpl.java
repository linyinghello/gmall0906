package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.SpuSaleAttr;
import com.linying.gmall.bean.SpuSaleAttrValue;
import com.linying.gmall.mamage.mapper.SpuSaleAttrMapper;
import com.linying.gmall.mamage.mapper.SpuSaleAttrValueMapper;
import com.linying.gmall.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuSaleAttrServiceImpl implements SpuSaleAttrService{

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<SpuSaleAttr> getSaleAttrService(String spuId) {
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.select(spuSaleAttr);
        for (SpuSaleAttr saleAttr : spuSaleAttrs) {
           String saleAttrId = saleAttr.getSaleAttrId();

           SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
           spuSaleAttrValue.setSpuId(spuId);
           spuSaleAttrValue.setSaleAttrId(saleAttrId);
           List<SpuSaleAttrValue> spuSaleAttrValues =  spuSaleAttrValueMapper.select(spuSaleAttrValue);
            saleAttr.setSpuSaleAttrValueList(spuSaleAttrValues);

        }

        return spuSaleAttrs;
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrListBySpuId(String spuId,String skuId) {
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.selectSaleAttrListBySpuId(spuId, skuId);
        return spuSaleAttrs;
    }
}
