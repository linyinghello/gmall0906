package com.linying.gmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.bean.*;



import com.linying.gmall.service.AttrService;
import com.linying.gmall.service.ListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class ListWebHandler {

    @Reference
    ListService listService;

    @Reference
    AttrService attrService;

    @RequestMapping("index.html")

    public String toIndexPage(){
        return "index";
    }


    @RequestMapping("list.html")

    public String toListPage(SkuLsParam skuLsParam,Model model){
        List<SkuLsInfo> skuLsInfoList = listService.list(skuLsParam);


        Set<String> valueIds = new HashSet<>();
        for (SkuLsInfo skuLsInfo : skuLsInfoList) {
            List<SkuLsAttrValue> skuAttrValueList = skuLsInfo.getSkuAttrValueList();
            for (SkuLsAttrValue skuLsAttrValue : skuAttrValueList) {
                String valueId = skuLsAttrValue.getValueId();
                valueIds.add(valueId);
            }
        }

        String join = StringUtils.join(valueIds, ",");
        List<BaseAttrInfo> attrList = new ArrayList<>();

        attrList = attrService.getAttrListByValueIds(join);


        model.addAttribute("skuLsInfoList",skuLsInfoList);

        List<BaseAttrValue> attrValueSelectedList = new ArrayList<>();

        String[] valueId = skuLsParam.getValueId();
        if(valueId != null && valueId.length>0){
            Iterator<BaseAttrInfo> iterator = attrList.iterator();
            while(iterator.hasNext()){
                List<BaseAttrValue> attrValueList = iterator.next().getAttrValueList();
                for (BaseAttrValue baseAttrValue : attrValueList) {
                    String id = baseAttrValue.getId();
                    for (String sid : valueId) {
                        if(id.equals(sid)){
                            attrValueSelectedList.add(baseAttrValue);
                            iterator.remove();
                        }

                    }
                }
            }
        }

        model.addAttribute("attrList",attrList);
        String urlParam = getMyUrlParam(skuLsParam);
        model.addAttribute("attrValueSelectedList",attrValueSelectedList);
        model.addAttribute("urlParam",urlParam);
        return "list";
    }

    private String getMyUrlParam(SkuLsParam skuLsParam) {
        String urlParam = "";
        String catalog3Id = skuLsParam.getCatalog3Id();
        String keyword = skuLsParam.getKeyword();
        String[] valueId = skuLsParam.getValueId();
        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" +catalog3Id;
        }

        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" +keyword;
        }

        if(valueId!=null&&valueId.length>0){
            for (String id : valueId) {
                urlParam = urlParam + "&valueId=" +id;
            }
        }


        return urlParam;
    }


    @RequestMapping("mianbao.html")
    public String toListPages(SkuLsParam skuLsParam,Model model){
       String[] valueIds = skuLsParam.getValueId();
        int length = valueIds.length-1;
        String id =   valueIds[length];





        List<String> list = new ArrayList<String>();
        for (int i=0; i<valueIds.length; i++) {
            list.add(valueIds[i]);
        }
        list.remove(length); //list.remove("Php")

        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String value = iterator.next();
            if(id.equals(value)){
                iterator.remove();
            }
        }

        if(list.size()!=0){
            String[] newStr =  list.toArray(new String[1]);
            skuLsParam.setValueId(newStr);
        } else{
            skuLsParam.setValueId(new String[0]);
        }



        String s = toListPage(skuLsParam, model);

           return "list";

    }
    }


