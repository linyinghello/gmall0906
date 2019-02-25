package com.linying.gmall.mamage.mapper;

import com.linying.gmall.bean.SkuInfo;
import com.linying.gmall.bean.SpuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuInfoMapper extends Mapper<SpuInfo> {

    void saveAllInformation(SpuInfo spuInfo);


}
