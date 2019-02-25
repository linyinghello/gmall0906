package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseAttrInfo;
import com.linying.gmall.bean.BaseCatalog1;
import com.linying.gmall.mamage.mapper.AttrInfoMapper;
import com.linying.gmall.service.AttrService;
import com.linying.gmall.mamage.mapper.AttrMapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    private AttrMapper attrMapper;

    @Autowired
    private AttrInfoMapper attrInfoMapper;


    public List<BaseCatalog1> getAttrListPage() {
        return attrMapper.selectAll();
    }

    @Override
    public List<BaseAttrInfo> getAttrListByValueIds(String join) {
       List<BaseAttrInfo> baseAttrInfos = attrInfoMapper.selectAttrListByValueIds(join);
        return baseAttrInfos;
    }
}


