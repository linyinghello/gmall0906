package com.linying.gmall.mamage.controller;

import com.linying.gmall.mamage.utils.ManageUploadUtil;

import com.linying.gmall.mamage.utils.UploadTest;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadImg {



    @RequestMapping("fileUpload")
    @ResponseBody
   public String fileUpload(@RequestParam("file") MultipartFile multiPartFile){
    //String imgUrl = ManageUploadUtil.imgUpolad(multiPartFile);


        String img = UploadTest.imgUpolad(multiPartFile);
       // System.out.println(img);
        //System.out.println(imgUrl);
        return img;
   }






}
