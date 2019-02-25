package com.linying.gmall.service;

import com.linying.gmall.bean.SpuImage;

import java.util.List;

public interface SpuImageService {
    List<SpuImage> getSpuImageLists(String spuId);
}
