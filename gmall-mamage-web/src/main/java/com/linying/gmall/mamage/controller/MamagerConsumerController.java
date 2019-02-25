package com.linying.gmall.mamage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MamagerConsumerController {
    @RequestMapping("index")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("attrListPage")
    public String toAttrListPage(){
        return "attrListPage";
    }


    @RequestMapping("attrSpuPage")
    public String toAttrSpuPage(){
        return "attrSpuPage";
    }

}
