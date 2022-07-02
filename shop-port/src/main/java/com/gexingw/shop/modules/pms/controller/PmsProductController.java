package com.gexingw.shop.modules.pms.controller;

import com.alibaba.fastjson.JSON;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.PmsProductOnSaleEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.es.ESProduct;
import com.gexingw.shop.modules.pms.dto.product.PmsProductPriceRequestParam;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.modules.pms.vo.PmsProductSkuVO;
import com.gexingw.shop.modules.pms.vo.PmsProductVO;
import com.gexingw.shop.utils.R;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GeXingW
 */
@RestController
@RequestMapping("product")
public class PmsProductController {

    @Autowired
    PmsProductService pmsProductService;

    @Autowired
    PmsProductSkuService pmsProductSkuService;

    @Autowired
    FileConfig fileConfig;

    @Autowired
    RestHighLevelClient client;

    @GetMapping("/search")
    public R search(PmsProductSearchRequestParam requestParam) throws IOException {
        // 根据标题搜索
        MatchQueryBuilder titleQuery = QueryBuilders.matchQuery("title", requestParam.getKeywords());

        // 根据SKU搜索
        TermQueryBuilder skuQuery = QueryBuilders.termQuery("skuOptions.attributeValues", requestParam.getKeywords());
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("skuOptions", new BoolQueryBuilder().should(skuQuery), ScoreMode.None);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(titleQuery).should(nestedQuery);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        // 设置分页
        searchSourceBuilder.from(requestParam.getFrom());
        searchSourceBuilder.size(requestParam.getSize());

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("product").source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", searchHits.getTotalHits().value);
        result.put("size", requestParam.getSize());
        result.put("page", requestParam.getPage());

        // 搜索到的商品
        ArrayList<ESProduct> records = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            records.add(JSON.parseObject(hit.getSourceAsString(), ESProduct.class));
        }

        result.put("records", records);

        return R.ok(result);
    }

    /**
     * 商品详情
     *
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public R info(@PathVariable String id) {
        PmsProduct pmsProduct = pmsProductService.getById(id);
        if (pmsProduct == null) {
            return R.ok(RespCode.PRODUCT_NOT_EXIST);
        }

        // 检查商品是否上架
        if (!PmsProductOnSaleEnum.ON_SALE.getCode().equals(pmsProduct.getOnSale())) {
            return R.ok(RespCode.RESOURCE_UNAVAILABLE.getCode(), "商品暂未上架！");
        }

        return R.ok(new PmsProductVO(pmsProduct).setProductPics(pmsProduct, fileConfig));
    }

    @PostMapping("/{id}/price")
    public R price(@PathVariable String id, @RequestBody PmsProductPriceRequestParam requestParam) {
        // 检查商品信息是否存在
        if (pmsProductService.getById(id) == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品不存在！");
        }

        // 获取商品价格和库存
        List<PmsProductPriceRequestParam.Option> spData = requestParam.getOptions().stream().sorted(Comparator.comparing(PmsProductPriceRequestParam.Option::getId))
                .collect(Collectors.toList());
        PmsProductSku productSku = pmsProductSkuService.getByProductIdAndSkuData(id, spData);

        // 查询不到该Sku信息，直接返回默认值
        if (productSku == null) {
            return R.ok(new PmsProductSkuVO());
        }

        return R.ok(new PmsProductSkuVO(productSku));
    }

}
