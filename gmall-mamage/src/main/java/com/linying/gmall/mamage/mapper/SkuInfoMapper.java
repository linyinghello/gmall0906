package com.linying.gmall.mamage.mapper;

import com.linying.gmall.bean.SkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuInfoMapper extends Mapper<SkuInfo> {
    List<SkuInfo> selectSkuSaleAttrValueListBySpu(String spuId);
}
