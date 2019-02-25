package com.linying.gmall.service;

import com.linying.gmall.bean.BaseCatalog2;
import com.linying.gmall.bean.BaseCatalog3;

import java.util.List;

public interface Ctg3SerVice {
    List<BaseCatalog3> getCtg3ForAttrList(String ctg2);
}
