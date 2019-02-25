package com.linying.gmall.mamage.Service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linying.gmall.bean.ComImg;
import com.linying.gmall.bean.ComMain;
import com.linying.gmall.bean.ComRep;
import com.linying.gmall.mamage.mapper.ComImgMapper;
import com.linying.gmall.mamage.mapper.ComMainMapper;
import com.linying.gmall.mamage.mapper.ComRefMapper;
import com.linying.gmall.service.ComMainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ComMainServiceImpl implements ComMainService {
    @Autowired
    private ComMainMapper comMainMapper;

    @Autowired
    private ComImgMapper comImgMapper;

    @Autowired
    private ComRefMapper comRefMapper;

    @Override
    public void uploadCommentAndImg(ComMain comMain) {
        comMainMapper.insertSelective(comMain);
        String comId = comMain.getComId();
        List<ComImg> comImgList = comMain.getComImgList();
        for (ComImg comImg : comImgList) {
            comImg.setComId(comId);
            comImgMapper.insert(comImg);
        }
    }

    @Override
    public List<ComMain> getCommentBySkuId(String skuId) {
        ComMain comMain = new ComMain();
        comMain.setSkuId(skuId);
       List<ComMain> comMains =  comMainMapper.select(comMain);

        for (ComMain comMain1 : comMains) {
            String comId = comMain1.getComId();
            ComImg comImg = new ComImg();
            comImg.setComId(comId);
            List<ComImg> comImgList = comImgMapper.select(comImg);
            comMain1.setComImgList(comImgList);

            ComRep comRep = new ComRep();
            comRep.setComId(comId);
            List<ComRep> comReps = comRefMapper.select(comRep);
            comMain1.setComRepList(comReps);
        }
        return comMains;
    }

    @Override
    public PageInfo<ComMain> getCommentBySkuIds(String skuId, Integer pageNo) {
        Integer pageSize = 2;
        PageHelper.startPage(pageNo,pageSize);

        ComMain comMain = new ComMain();
        comMain.setSkuId(skuId);
        List<ComMain> comMains =  comMainMapper.select(comMain);

        for (ComMain comMain1 : comMains) {
            String comId = comMain1.getComId();
            ComImg comImg = new ComImg();
            comImg.setComId(comId);
            List<ComImg> comImgList = comImgMapper.select(comImg);
            comMain1.setComImgList(comImgList);

            ComRep comRep = new ComRep();
            comRep.setComId(comId);
            List<ComRep> comReps = comRefMapper.select(comRep);
            comMain1.setComRepList(comReps);
        }
        PageInfo<ComMain> pageInfo = new PageInfo<>(comMains);
        return pageInfo;
    }

    @Override
    public String getComCount() {
        ComMain comMain = new ComMain();
          Integer count = comMainMapper.selectCount(comMain);
          String comCount = String.valueOf(count);
          return  comCount;
    }
}
