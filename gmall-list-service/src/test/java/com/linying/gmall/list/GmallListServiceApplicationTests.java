package com.linying.gmall.list;

import com.alibaba.dubbo.config.annotation.Reference;
import com.linying.gmall.bean.SkuInfo;
import com.linying.gmall.bean.SkuLsInfo;
import com.linying.gmall.service.SkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallListServiceApplicationTests {

	@Autowired
	JestClient jestClient;

	@Reference
	SkuInfoService skuInfoService;

	@Test
	public void context() throws IOException {
		List<SkuInfo> skuInfos = skuInfoService.SkuListByCatalog3Id("9");

		for (SkuInfo skuInfo : skuInfos) {
			SkuLsInfo skuLsInfo = new SkuLsInfo();
			BeanUtils.copyProperties(skuInfo,skuLsInfo);
			Index index = new Index.Builder(skuLsInfo).index("gmall0906").type("SkuLsInfo").id(skuLsInfo.getId()).build();
			jestClient.execute(index);
		}
		
		System.out.println(1);

	}

	@Test
	public void contextLoads() throws IOException {
		//System.err.println(jestClient);
		Search search = new Search.Builder("{\n" +
				"  \"query\":{\n" +
				"    \"bool\":{\n" +
				"     \n" +
				"      \"filter\":[{\n" +
				"        \"terms\":{\n" +
				"          \"actorList.id\":[\"3\",\"4\"]\n" +
				"        }\n" +
				"      }\n" +
				"      \n" +
				"      ]\n" +
				"    }\n" +
				"  }\n" +
				"}\n").addIndex("movie_index").addType("movie").build();

		SearchResult reslut = jestClient.execute(search);
		System.err.println(reslut);
		List<SearchResult.Hit<HashMap, Void>> hits = reslut.getHits(HashMap.class);
		for (SearchResult.Hit<HashMap, Void> hit : hits) {
			HashMap source = hit.source;
			System.err.println(source);

		}
	}

}

