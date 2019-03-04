package com.linying.gmall.list;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.linying.gmall.bean.BaseCatalog1;
import com.linying.gmall.service.CatalogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;
import java.util.logging.Filter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListWebApplicationTests {

	@Reference
	private CatalogService catalogService;

	@Test
	public void contextLoads() throws IOException {

		List<BaseCatalog1> catalogList = catalogService.getCatalogList();
		String s = JSON.toJSONString(catalogList);

		FileWriter writer = new FileWriter("catalog.json");
		writer.write(s);
		writer.close();



	}

}

