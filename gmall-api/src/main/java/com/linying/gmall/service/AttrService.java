package com.linying.gmall.service;

import com.linying.gmall.bean.BaseAttrInfo;
import com.linying.gmall.bean.BaseAttrValue;
import com.linying.gmall.bean.BaseCatalog1;

import java.util.List;

public interface AttrService{
    List<BaseCatalog1> getAttrListPage();
    List<BaseAttrInfo> getAttrListByValueIds(String join);

}
