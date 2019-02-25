package com.linying.gmall.item.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.linying.gmall.annotations.LoginRequired;
import com.linying.gmall.bean.*;
import com.linying.gmall.service.*;
import com.linying.gmall.util.ManageUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Reference
    private ComMainService comMainService;

    @Reference
    private UserInfoService userInfoService;

    @Reference
    private SkuInfoService skuInfoService;

    @Reference
    private ComRepService comRepService;


    @Reference
    private SpuSaleAttrService spuSaleAttrService;


    @LoginRequired(isNeedLogin = true)
    @RequestMapping("{skuId}.html")
    public String toItemPage(@PathVariable("skuId") String skuId,Model model,HttpServletRequest request){
        String  userId = (String)request.getAttribute("userId");
        System.out.println(userId);
        UserInfo userInfoByUserId = userInfoService.getUserInfoByUserId(userId);
        System.out.println(userInfoByUserId);
        String nickName = userInfoByUserId.getNickName();
        model.addAttribute("nickName",nickName);
        System.out.println(nickName);
        SkuInfo skuBySkuId = skuInfoService.item(skuId);
        model.addAttribute("skuInfo",skuBySkuId);
        String spuId = skuBySkuId.getSpuId();
        List<SpuSaleAttr> spuSaleAttr = spuSaleAttrService.getSaleAttrListBySpuId(spuId, skuId);
        model.addAttribute("spuSaleAttrListCheckBySku",spuSaleAttr);


        List<SkuInfo> skuSaleAttrValueListBySpu = skuInfoService.getSkuSaleAttrValueListBySpu(spuId);

       Map<String,String> skuMap = new HashMap<>();
        for (SkuInfo skuInfo : skuSaleAttrValueListBySpu) {
            String v = skuInfo.getId();
            List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
                String k = "";
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                String saleAttrValueId = skuSaleAttrValue.getSaleAttrValueId();
                k=k+"|"+saleAttrValueId;
            }
            skuMap.put(k,v);
        }

        model.addAttribute("skuMap", JSON.toJSONString(skuMap));


      List<ComMain> comMains =   comMainService.getCommentBySkuId(skuId);
            model.addAttribute("comMains",comMains);

        String count =  comMainService.getComCount();
        model.addAttribute("comCount",count);
        return "item";

    }

  @RequestMapping("getAllComment")
    @ResponseBody
    public PageInfo<ComMain> getAllComment(String skuId, Integer pageNo){
        PageInfo<ComMain> pageInfo =   comMainService.getCommentBySkuIds(skuId,pageNo);
        return pageInfo;
    }

   @RequestMapping("fileUpload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile multiPartFile){
        String imgUrl = ManageUploadUtil.imgUpolad(multiPartFile);
        System.out.println(imgUrl);
            return imgUrl;
    }


    @RequestMapping("uploadCommentAndImg")
    @ResponseBody
    public String uploadCommentAndImg(ComMain comMain, HttpServletRequest request,Model model){

        comMainService.uploadCommentAndImg(comMain);

        return "success";
    }


    @RequestMapping("uploadRepaly")
    @ResponseBody
    public String uploadRepaly(ComRep comRep){
        comRepService.addUploadReplay(comRep);
        return "success";
    }

    @RequestMapping("getComCount")
    @ResponseBody
    public String getComCount(Model model){
       String count =  comMainService.getComCount();
        model.addAttribute("comCount",count);
        return count;
    }


}
