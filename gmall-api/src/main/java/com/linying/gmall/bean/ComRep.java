package com.linying.gmall.bean;

import javax.persistence.Column;
import java.io.Serializable;

public class ComRep implements Serializable {
    @Column
    private String id;
    @Column
    private String comId;

    @Column
    private String replay;

    @Column
    private String repWho;

    @Column
    private String comName;

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    @Override
    public String toString() {
        return "ComRep{" +
                "id='" + id + '\'' +
                ", comId='" + comId + '\'' +
                ", replay='" + replay + '\'' +
                ", repWho='" + repWho + '\'' +
                '}';
    }

    public ComRep() {
    }

    public ComRep(String id, String comId, String replay, String repWho, String comName) {
        this.id = id;
        this.comId = comId;
        this.replay = replay;
        this.repWho = repWho;
        this.comName = comName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public String getRepWho() {
        return repWho;
    }

    public void setRepWho(String repWho) {
        this.repWho = repWho;
    }
}
