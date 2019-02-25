package com.linying.gmall.service;

import com.linying.gmall.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AttrListService {
    List<BaseAttrInfo> getAttrList(String catalog3Id);

}
