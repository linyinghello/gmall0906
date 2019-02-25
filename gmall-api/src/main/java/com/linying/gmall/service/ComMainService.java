package com.linying.gmall.service;

import com.github.pagehelper.PageInfo;
import com.linying.gmall.bean.ComMain;

import java.util.List;

public interface ComMainService {
    void uploadCommentAndImg(ComMain comMain);

    List<ComMain> getCommentBySkuId(String skuId);

    PageInfo<ComMain> getCommentBySkuIds(String skuId, Integer pageNo);

    String getComCount();
}
