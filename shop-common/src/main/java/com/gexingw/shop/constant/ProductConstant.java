package com.gexingw.shop.constant;

public interface ProductConstant {
    // 商品上架
    Integer ON_SALE = 1;

    // 商品下架
    Integer OFF_SALE = 0;

    String REDIS_PRODUCT_FORMAT = "product:%s";

    // 商品分类的树形结构
    String REDIS_PRODUCT_CATEGORY_TREE = "product:category_tree";

    // 商品分类Redis缓存
    String REDIS_PRODUCT_CATEGORY_FORMAT = "product:category:%s";

    // 商品属性组 redis缓存
    String REDIS_PRODUCT_ATTRIBUTE_GROUP_FORMAT = "product:attribute-group:%s";
}
