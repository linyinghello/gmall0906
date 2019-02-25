package com.linying.gmall.list.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.linying.gmall.bean.SkuLsInfo;
import com.linying.gmall.bean.SkuLsParam;
import com.linying.gmall.service.ListService;
import io.searchbox.client.JestClient;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    JestClient jestClient;


    @Override
    public List<SkuLsInfo> list(SkuLsParam skuLsParam) {
        List<SkuLsInfo> skuLsInfos = new ArrayList<>();
        String dsl = getMyDsl(skuLsParam);
        System.err.println(dsl);

        Search builder = new Search.Builder(dsl).addIndex("gmall0906").addType("SkuLsInfo").build();
        SearchResult execute = null;
        try {
            execute = jestClient.execute(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long total = execute.getTotal();

        if (total > 0) {
            List<SearchResult.Hit<SkuLsInfo, Void>> hits = execute.getHits(SkuLsInfo.class);

            for (SearchResult.Hit<SkuLsInfo, Void> hit : hits) {
                SkuLsInfo source = hit.source;
                if(hit.highlight != null && hit.highlight.size()>0){
                    List<String> list = hit.highlight.get("skuName");
                    String skuNameHl = list.get(0);
                    source.setSkuName(skuNameHl);
                }
                skuLsInfos.add(source);
            }
        }
        return skuLsInfos;
    }


    public String getMyDsl(SkuLsParam skuLsParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        String catalog3Id = skuLsParam.getCatalog3Id();
        if (StringUtils.isNotBlank(catalog3Id)) {
            TermQueryBuilder temQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(temQueryBuilder);
        }

        String[] valueId = skuLsParam.getValueId();
        if (valueId != null && valueId.length > 0) {
            for (String id : valueId) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", id);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        String keyword = skuLsParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);


        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        return searchSourceBuilder.toString();
    }
}

