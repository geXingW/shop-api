package com.gexingw.shop.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "product")
public class ESProduct {
//    @Id
//    @Field(type = FieldType.Long, index = false)
//    private long skuId = 0L;

    @Field(type = FieldType.Long, index = false)
    private String id = "0";

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, index = false)
    private BigDecimal salePrice;

    @Field(type = FieldType.Text, index = false)
    private String pic;

    @Field(type = FieldType.Text, index = false)
    private List<String> albums = new ArrayList<>();

    @Field(type = FieldType.Long, index = false)
    private Long brandId = 0L;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Text, index = false)
    private String brandIcon;

    @Field(type = FieldType.Nested)
    private List<ESProductSku> skuOptions = new ArrayList<>();

    @Data
    public static class ESProductSku {
        @Field(type = FieldType.Long, index = false)
        private Long attributeId = 0L;

        @Field(type = FieldType.Keyword, index = false)
        private String attributeName;

        @Field(type = FieldType.Keyword)
        private List<String> attributeValues;
    }
}
