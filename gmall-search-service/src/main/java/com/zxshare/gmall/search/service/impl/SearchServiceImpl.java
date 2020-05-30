package com.zxshare.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxshare.gmall.bean.PmsSearchSkuInfo;
import com.zxshare.gmall.param.PmsSearchParam;
import com.zxshare.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;


    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) throws IOException {

        //Search
        String search = gerSearchDsl(pmsSearchParam);
        String keyword = pmsSearchParam.getKeyword();
        System.out.println("=====================================");
        System.out.println(search);
        //返回结果对象
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();


        Search build = new Search.Builder(search).addIndex("gmall0105").addType("PmsSkuInfo").build();

        //E查询完返回的结果集
        SearchResult execute = jestClient.execute(build);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);


            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                PmsSearchSkuInfo source = hit.source;
                pmsSearchSkuInfos.add(source);

                if (StringUtils.isNotBlank(keyword)) {
                    Map<String, List<String>> highlight = hit.highlight;

                    String skuName = highlight.get("skuName").get(0);
                    source.setSkuName(skuName);
                }
            }

        return pmsSearchSkuInfos;
    }

    private String gerSearchDsl(PmsSearchParam pmsSearchParam) {

        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        //jest 的查询工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", pmsSkuAttrValue);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        if (StringUtil.isNotBlank(catalog3Id)) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }


        //must
        if (StringUtil.isNotBlank(keyword)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
            //highlight
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            highlightBuilder.preTags("<span style='color:red;'>");
            highlightBuilder.field("skuName");
            highlightBuilder.postTags("</span>");

            searchSourceBuilder.highlight(highlightBuilder);
        }

        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //size
        searchSourceBuilder.size(20);
        //from
        searchSourceBuilder.from(0);


        //sort
        searchSourceBuilder.sort("id", SortOrder.DESC);

        return searchSourceBuilder.toString();
    }
}
