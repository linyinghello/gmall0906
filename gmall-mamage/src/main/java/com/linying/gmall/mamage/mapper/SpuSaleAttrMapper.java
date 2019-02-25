package com.linying.gmall.mamage.mapper;

import com.linying.gmall.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    List<SpuSaleAttr>  selectSaleAttrListBySpuId(@Param("spuId")String spuId, @Param("skuId") String skuId);
}
