package com.linying.gmall.service;

import com.linying.gmall.bean.BaseAttrInfo;

public interface AttrInfoService {
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
    String getAttrName(String id);
    void updateAttr(BaseAttrInfo baseAttrInfo);
    void removeAttr(String id);
}
