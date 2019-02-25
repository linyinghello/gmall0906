package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.*;
import com.linying.gmall.mamage.mapper.*;
import com.linying.gmall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuInfoServiceImpl implements SpuInfoService{
    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private AttrValueMapper attrValueMappe;

    @Override
    public List<SpuInfo> getSpuInfoList(String catalogSpu3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalogSpu3Id);
       List<SpuInfo> spuInfos = spuInfoMapper.select(spuInfo);


        return spuInfos;
    }

    @Override
    public void saveAllInformation(SpuInfo spuInfo) {
        spuInfoMapper.insertSelective(spuInfo);
        String spuId = spuInfo.getId();
        List<SpuImage> spuImages =  spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImages) {
            spuImage.setSpuId(spuId);

            spuImageMapper.insertSelective(spuImage);

        }

        //插入spuSaleAttr
        List<SpuSaleAttr> spuSaleAttrList= spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insertSelective(spuSaleAttr);

           List<SpuSaleAttrValue> spuSaleAttrValues  = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValues) {
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
            }
        }



    }
}
