package com.linying.gmall.mamage.Service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseCatalog1;
import com.linying.gmall.bean.BaseCatalog2;
import com.linying.gmall.bean.BaseCatalog3;
import com.linying.gmall.mamage.mapper.CatalogMapper;
import com.linying.gmall.mamage.mapper.Ctg2Mapper;
import com.linying.gmall.mamage.mapper.Ctg3Mapper;
import com.linying.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService{

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private Ctg2Mapper ctg2Mapper;

    @Autowired
    private Ctg3Mapper ctg3Mapper;

    @Override
    public List<BaseCatalog1> getCatalogList() {
        List<BaseCatalog1> baseCatalog1s = catalogMapper.selectAll();
        for (BaseCatalog1 baseCatalog1 : baseCatalog1s) {
            String id = baseCatalog1.getId();
            BaseCatalog2 baseCatalog2 = new BaseCatalog2();
            baseCatalog2.setCatalog1Id(id);
            List<BaseCatalog2> baseCatalog2s = ctg2Mapper.select(baseCatalog2);
            baseCatalog1.setBaseCatalog2List(baseCatalog2s);

            for (BaseCatalog2 catalog2 : baseCatalog2s) {
                String cat2Id = catalog2.getId();
                BaseCatalog3 catalog3 = new BaseCatalog3();
                catalog3.setCatalog2Id(cat2Id);

                List<BaseCatalog3> baseCatalog3s = ctg3Mapper.select(catalog3);
                catalog2.setBaseCatalog3List(baseCatalog3s);
            }
        }
                return baseCatalog1s;
    }
}
