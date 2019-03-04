package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.BaseAttrInfo;
import com.linying.gmall.bean.BaseAttrValue;
import com.linying.gmall.mamage.mapper.AttrInfoMapper;
import com.linying.gmall.mamage.mapper.AttrValueMapper;
import com.linying.gmall.service.AttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrInfoServiceImpl implements AttrInfoService {



    @Autowired
    private AttrInfoMapper attrInfoMappe;

    @Autowired
    private AttrValueMapper attrValueMappe;
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        attrInfoMappe.insertSelective(baseAttrInfo);
        String attrId = baseAttrInfo.getId();
      List<BaseAttrValue> baseAttrValues= baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : baseAttrValues) {
            baseAttrValue.setAttrId(attrId);
            attrValueMappe.insertSelective(baseAttrValue);
        }

    }
    @Override
    public String getAttrName(String id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setId(id);
        List<BaseAttrInfo> baseAttrInfos = attrInfoMappe.select(baseAttrInfo);
        BaseAttrInfo baseAttrInfos1 = baseAttrInfos.get(0);
        String attrName = baseAttrInfos1.getAttrName();
        return attrName;
    }

    @Override
    public void updateAttr(BaseAttrInfo baseAttrInfo) {
        attrInfoMappe.updateByPrimaryKeySelective(baseAttrInfo);
    }

    @Override
    public void removeAttr(String id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setId(id);
        attrInfoMappe.delete(baseAttrInfo);
    }
}
