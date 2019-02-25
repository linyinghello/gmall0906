package com.linying.gmall.mamage.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public class UploadTest {

 /*  @Autowired
   private static StorageClient storageClient;*/

    public static String imgUpolad(MultipartFile multipartFile){

        String originalFilename = multipartFile.getOriginalFilename();
        StorageClientFactory storageClientFactory = new StorageClientFactory();
        StorageClient storageClient = storageClientFactory.getObject();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        String[] resultArray = new String[0];
        try {
            resultArray = storageClient.upload_file(multipartFile.getBytes(), extName, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        String groupName = resultArray[0];
        String remoteFileName = resultArray[1];
        String imgUrl = "http://192.168.75.75/"+groupName+"/"+remoteFileName;


        return imgUrl;
    }
}
