package com.linying.gmall.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class ComMain implements Serializable {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String comId;

    @Column
    private String ComName;

    @Column
    private String comment;

    @Column
    private String comImgId;

    @Transient
    private List<ComImg> comImgList;
    @Transient
    private List<ComRep> comRepList;

    @Column
    private String skuId;

    @Override
    public String toString() {
        return "ComMain{" +
                "comId='" + comId + '\'' +
                ", ComName='" + ComName + '\'' +
                ", comment='" + comment + '\'' +
                ", comImgId='" + comImgId + '\'' +
                ", comImgList=" + comImgList +
                ", comRepList=" + comRepList +
                ", skuId='" + skuId + '\'' +
                '}';
    }

    public ComMain() {
    }

    public ComMain(String comId, String comName, String comment, String comImgId, List<ComImg> comImgList, List<ComRep> comRepList, String skuId) {
        this.comId = comId;
        ComName = comName;
        this.comment = comment;
        this.comImgId = comImgId;
        this.comImgList = comImgList;
        this.comRepList = comRepList;
        this.skuId = skuId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getComName() {
        return ComName;
    }

    public void setComName(String comName) {
        ComName = comName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComImgId() {
        return comImgId;
    }

    public void setComImgId(String comImgId) {
        this.comImgId = comImgId;
    }

    public List<ComImg> getComImgList() {
        return comImgList;
    }

    public void setComImgList(List<ComImg> comImgList) {
        this.comImgList = comImgList;
    }

    public List<ComRep> getComRepList() {
        return comRepList;
    }

    public void setComRepList(List<ComRep> comRepList) {
        this.comRepList = comRepList;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
