package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.ComRep;
import com.linying.gmall.mamage.mapper.ComRefMapper;
import com.linying.gmall.service.ComRepService;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class ComRepServiceImpl implements ComRepService {
    @Autowired
    private ComRefMapper comRefMapper;

    @Override
    public void addUploadReplay(ComRep comRep) {
        comRefMapper.insertSelective(comRep);
    }
}
