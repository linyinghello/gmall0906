package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseAttrInfo;
import com.linying.gmall.bean.BaseAttrValue;
import com.linying.gmall.mamage.mapper.AttrListMapper;
import com.linying.gmall.mamage.mapper.AttrValueMapper;
import com.linying.gmall.service.AttrListService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttrListServiceImpl implements AttrListService {
    @Autowired
    private AttrListMapper attrListMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;
    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfos =  attrListMapper.select(baseAttrInfo);

       for (BaseAttrInfo attrInfo : baseAttrInfos) {
           List<BaseAttrValue> baseAttrValues = new ArrayList<>();
            String id =attrInfo.getId();
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(id);
             baseAttrValues=attrValueMapper.select(baseAttrValue);
            attrInfo.setAttrValueList(baseAttrValues);
        }
        return baseAttrInfos;
    }


}
