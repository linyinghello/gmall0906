package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.SpuImage;
import com.linying.gmall.mamage.mapper.SpuImageMapper;
import com.linying.gmall.service.SpuImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuImageServiceImpl implements SpuImageService {

    @Autowired
    private SpuImageMapper spuImageMapper;
    @Override
    public List<SpuImage> getSpuImageLists(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        List<SpuImage> spuImages = spuImageMapper.select(spuImage);
        return spuImages;
    }
}
