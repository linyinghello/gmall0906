package com.linying.gmall.mamage.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;


@Component
public class StorageClientFactory implements FactoryBean<StorageClient> {


/*    @Value("${linying.tracker.config.location}")
    private String configLocation;*/

    @Override
    public StorageClient getObject()  {




        //2.获取tracker.conf的绝对物理路径
       /* URL url = StorageClientFactory.class.getResource(configLocation);
        String absolutePath = url.getPath();
        System.out.println("absolutePath="+absolutePath);*/

        //3.初始化操作
        try {
            ClientGlobal.init("tracker.conf");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        //4.创建对象
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);

        //5.将创建好的对象返回
        return storageClient;

    }

    @Override
    public Class<?> getObjectType() {
        return StorageClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
