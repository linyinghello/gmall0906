package com.linying.gmall.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @param
 * @return
 */
public class BaseCatalog2 implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String name;
    @Column
    private String catalog1Id;

    public List<BaseCatalog3> getBaseCatalog3List() {
        return baseCatalog3List;
    }

    public void setBaseCatalog3List(List<BaseCatalog3> baseCatalog3List) {
        this.baseCatalog3List = baseCatalog3List;
    }

    List<BaseCatalog3> baseCatalog3List;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog1Id() {
        return catalog1Id;
    }

    public void setCatalog1Id(String catalog1Id) {
        this.catalog1Id = catalog1Id;
    }
}
