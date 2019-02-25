package com.linying.gmall.mamage.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.bean.*;
import com.linying.gmall.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class AttrController {

    @Reference
    private SkuInfoService skuInfoService;



    @Reference
    private SpuSaleAttrService spuSaleAttrService;

@Reference
private AttrService attrService;

@Reference
private Ctg2SerVice ctg2SerVice;
    @Reference
    private Ctg3SerVice ctg3SerVice;

    @Reference
    private AttrListService attrListService;

    @Reference
    private AttrInfoService attrInfoService;


    @Reference
    private AttrValueService attrValueService;

    @Reference
    private SpuInfoService spuInfoService;

    @Reference
    private SaleAttrService saleAttrService;

    @Reference
    private SpuImageService spuImageService;

    @RequestMapping("ctg1ForAttrList")
    @ResponseBody
    public List<BaseCatalog1> getCtg1ForAttrList(){

         List<BaseCatalog1> baseCatalog1s = attrService.getAttrListPage();
         return baseCatalog1s;
    }

    @RequestMapping("ctg2ForAttrList")
    @ResponseBody
    public List<BaseCatalog2> getCtg2ForAttrList(String ctg1){
        List<BaseCatalog2> baseCatalog2s = ctg2SerVice.getCtg2ForAttrList(ctg1);
        return baseCatalog2s;
    }

    @RequestMapping("ctg3ForAttrList")
    @ResponseBody
    public List<BaseCatalog3> getCtg3ForAttrList(String ctg2){
        List<BaseCatalog3> baseCatalog3s = ctg3SerVice.getCtg3ForAttrList(ctg2);
        return baseCatalog3s;
    }

    @RequestMapping("getAttrList")
    @ResponseBody
    public List<BaseAttrInfo> getAttrList(String catalog3Id){

        return attrListService.getAttrList(catalog3Id);
    }

    @RequestMapping("saveAttr")
    @ResponseBody
    public String saveAttr(BaseAttrInfo baseAttrInfo){
        attrInfoService.saveAttrInfo(baseAttrInfo);
        return "success";
    }

    @RequestMapping("ctg1ForSpuList")
    @ResponseBody
    public List<BaseCatalog1> getCtg1ForSpuList(){

        List<BaseCatalog1> baseCatalog1s = attrService.getAttrListPage();
        return baseCatalog1s;
    }

    @RequestMapping("ctg2ForSpuList")
    @ResponseBody
    public List<BaseCatalog2> getCtg2ForSpuList(String ctg1){
        List<BaseCatalog2> baseCatalog2s = ctg2SerVice.getCtg2ForAttrList(ctg1);
        return baseCatalog2s;
    }

    @RequestMapping("ctg3ForSpuList")
    @ResponseBody
    public List<BaseCatalog3> getCtg3ForSpuList(String ctg2){
        List<BaseCatalog3> baseCatalog3s = ctg3SerVice.getCtg3ForAttrList(ctg2);
        return baseCatalog3s;
    }

    @RequestMapping("getSpuList")
    @ResponseBody
    public List<SpuInfo> getSpuList(String catalogSpu3Id){

        return spuInfoService.getSpuInfoList(catalogSpu3Id);
    }

    @RequestMapping("beforeEditAttr")
    @ResponseBody
    public String beforeEditAttr(String id){
        String attrName = attrInfoService.getAttrName(id);
       return attrName;
    }

    @RequestMapping("editAttrSave")
    @ResponseBody
    public String editAttrSave(BaseAttrInfo baseAttrInfo){
        attrInfoService.updateAttr(baseAttrInfo);
        return "success";
    }

    @RequestMapping("removeAttr")
    @ResponseBody
    public String removeAttr(String id){
        attrInfoService.removeAttr(id);
        return "success";
    }

    @RequestMapping("getSaleAttr")
    @ResponseBody
    public List<BaseSaleAttr> getSaleAttr(){

        return saleAttrService.getSaleAttrList();
    }

    @RequestMapping("saveSupAttr")
    @ResponseBody
    public String saveSupAttr(SpuInfo spuInfo){
        spuInfoService.saveAllInformation(spuInfo);
        return "success";
    }




    @RequestMapping("spuSaleAttrLists")
    @ResponseBody
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        List<SpuSaleAttr> spuSaleAttrs =  spuSaleAttrService.getSaleAttrService(spuId);
        return spuSaleAttrs;
    }

    @RequestMapping("spuImgeLists")
    @ResponseBody
    public List<SpuImage> getSpuImgeLists(String spuId){
        List<SpuImage> spuImages = spuImageService.getSpuImageLists(spuId);
        return spuImages;
    }


    @RequestMapping("saveSku")
    @ResponseBody
    public String getsaveSku(SkuInfo skuInfo){
        System.out.println(skuInfo);

        skuInfoService.saveSkuInfo(skuInfo);
        return "success";

    }

    @RequestMapping("skuInfoListBySpu")
    @ResponseBody
    public List<SkuInfo> skuInfoListBySpu(String spuId){
        List<SkuInfo> spuInfoService = skuInfoService.getSpuInfoService(spuId);
        return spuInfoService;
    }


    @RequestMapping("editSkuList")
    @ResponseBody
    public String editSkuListJson(SkuInfo skuInfo){
        skuInfoService.updateEditSkuListJson(skuInfo);
        return "success";
    }
}
