package com.linying.gmall.bean;

import javax.persistence.Column;
import java.io.Serializable;

public class ComImg implements Serializable {

    @Column
    private String imgId;
    @Column
    private String comId;
    @Column
    private String imgUrl;

    public ComImg() {
    }

    @Override
    public String toString() {
        return "ComImg{" +
                "imgId='" + imgId + '\'' +
                ", comId='" + comId + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public ComImg(String imgId, String comId, String imgUrl) {
        this.imgId = imgId;
        this.comId = comId;
        this.imgUrl = imgUrl;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
