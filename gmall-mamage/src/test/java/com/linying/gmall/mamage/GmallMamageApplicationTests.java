package com.linying.gmall.mamage;

import com.linying.gmall.mamage.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallMamageApplicationTests {

	@Autowired
	private RedisUtil redisUtil;


	@Autowired
	JestClient jestClient;


	@Test
	public void contextLoads() {
		Jedis jedis = redisUtil.getJedis();
		String ping = jedis.ping();
		System.out.println(ping);

	}


	@Test
	public void contextLoadsssss() throws IOException {
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

