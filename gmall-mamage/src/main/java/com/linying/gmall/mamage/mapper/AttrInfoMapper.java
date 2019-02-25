package com.linying.gmall.mamage.mapper;

import com.linying.gmall.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AttrInfoMapper extends Mapper<BaseAttrInfo> {
    List<BaseAttrInfo> selectAttrListByValueIds(@Param("join")String join);
}
