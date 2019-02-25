package com.linying.gmall.mamage.Service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseCatalog2;
import com.linying.gmall.bean.BaseCatalog3;
import com.linying.gmall.mamage.mapper.Ctg2Mapper;
import com.linying.gmall.mamage.mapper.Ctg3Mapper;
import com.linying.gmall.service.Ctg2SerVice;
import com.linying.gmall.service.Ctg3SerVice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class Ctg3ServiceImpl implements Ctg3SerVice {

    @Autowired
    private Ctg3Mapper ctg3Mapper;
    @Override
    public List<BaseCatalog3> getCtg3ForAttrList(String ctg2) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(ctg2);
        return ctg3Mapper.select(baseCatalog3);
    }
}