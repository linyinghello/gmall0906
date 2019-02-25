package com.linying.gmall.service;

import com.linying.gmall.bean.SkuLsInfo;
import com.linying.gmall.bean.SkuLsParam;

import java.util.List;

public interface ListService {
    List<SkuLsInfo> list(SkuLsParam skuLsParam);
}
