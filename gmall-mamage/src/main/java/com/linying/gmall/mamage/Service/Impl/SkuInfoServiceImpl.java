package com.linying.gmall.mamage.Service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.SkuAttrValue;
import com.linying.gmall.bean.SkuImage;
import com.linying.gmall.bean.SkuInfo;
import com.linying.gmall.bean.SkuSaleAttrValue;
import com.linying.gmall.mamage.mapper.SkuAttrValueMapper;
import com.linying.gmall.mamage.mapper.SkuImageMapper;
import com.linying.gmall.mamage.mapper.SkuInfoMapper;
import com.linying.gmall.mamage.mapper.SkuSaleAttrValueMapper;
import com.linying.gmall.mamage.util.RedisUtil;
import com.linying.gmall.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class SkuInfoServiceImpl implements SkuInfoService{

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SkuImageMapper skuImageMapper;
    
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        skuInfoMapper.insertSelective(skuInfo);
        String skuInfoId = skuInfo.getId();

        //插入skuImage
        List<SkuImage> skuImages = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImages) {
            skuImage.setSkuId(skuInfoId);
            skuImageMapper.insertSelective(skuImage);
        }

        //spuAttrValue

        List<SkuAttrValue> skuAttrValues = skuInfo.getSkuAttrValueList();

        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            skuAttrValue.setSkuId(skuInfoId);
            String id = skuAttrValue.getValueId();
            if(id ==""){
                id = "1";
                skuAttrValue.setValueId(id);
            }
            skuAttrValueMapper.insertSelective(skuAttrValue);
        }


        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuInfoId);
            skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);

        }

    }

    @Override
    public List<SkuInfo> getSpuInfoService(String spuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSpuId(spuId);
        List<SkuInfo> skuInfos = skuInfoMapper.select(skuInfo);
        return skuInfos;
    }

    @Override
    public SkuInfo getSkuBySkuId(String skuId) {
        SkuInfo skuInfo1 = new SkuInfo();
        skuInfo1.setId(skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfo1);
        return skuInfo;
    }
    @Override
    public SkuInfo item(String skuId){
        SkuInfo skuInfo = null;
        Jedis jedis = redisUtil.getJedis();
        String skuInfoStr = jedis.get("sku:"+skuId+":info");
        skuInfo=JSON.parseObject(skuInfoStr,SkuInfo.class);
        if(skuInfoStr==null){
            System.out.println("缓存中没有要到数据库中拿");
            skuInfo = getSkuBySkuId(skuId);
            jedis.set("sku:"+skuId+":info", JSON.toJSONString(skuInfo));
        }
        jedis.close();
        return skuInfo;
    }

    @Override
    public List<SkuInfo> SkuListByCatalog3Id(String catalog3Id) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setCatalog3Id(catalog3Id);
        List<SkuInfo> skuInfos = skuInfoMapper.select(skuInfo);
        for (SkuInfo info : skuInfos) {
            SkuAttrValue skuAttrValue = new SkuAttrValue();
            skuAttrValue.setSkuId(info.getId());
            List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);
            info.setSkuAttrValueList(skuAttrValues);
        }
        return skuInfos;
    }

    @Override
    public void updateEditSkuListJson(SkuInfo skuInfo) {
        skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
    }

    @Override
    public  List<SkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {
      List<SkuInfo> skuInfos =   skuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);
      return skuInfos;
    }
}
