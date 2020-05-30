package com.zxshare.gmall.search;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zxshare.gmall.bean.PmsSearchSkuInfo;
import com.zxshare.gmall.bean.PmsSkuInfo;
import com.zxshare.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    SkuService skuService;


    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {

        //es 复杂查询

        //jest 的查询工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("","");
        boolQueryBuilder.filter(termQueryBuilder);

        TermQueryBuilder termQueryBuilder2 = new TermQueryBuilder("","");
        boolQueryBuilder.filter(termQueryBuilder);

        TermsQueryBuilder termsQueryBuilder3 = new TermsQueryBuilder("","");
        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("","");
        boolQueryBuilder.must(matchQueryBuilder);
        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //size
        searchSourceBuilder.size(20);
        //from
        searchSourceBuilder.from(0);

        //highlight
        searchSourceBuilder.highlight(null);

        String search = searchSourceBuilder.toString();

        //返回结果对象
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        Search build = new Search.Builder(search).addIndex("gmall0105").addType(null).build();

        SearchResult execute = jestClient.execute(build);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;

            pmsSearchSkuInfos.add(source);
        }
    }

    @Test
    public void putCache() throws IOException {
        //mysql 数据
        List<PmsSkuInfo> allSku = skuService.getAllSku();

        //转化
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : allSku) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index build = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            jestClient.execute(build);
        }
    }

}
