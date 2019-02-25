package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseCatalog2;
import com.linying.gmall.mamage.mapper.Ctg2Mapper;
import com.linying.gmall.service.Ctg2SerVice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class Ctg2ServiceImpl implements Ctg2SerVice {

    @Autowired
    private Ctg2Mapper ctg2Mapper;
    @Override
    public List<BaseCatalog2> getCtg2ForAttrList(String ctg1) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(ctg1);
        return ctg2Mapper.select(baseCatalog2);
    }
}
