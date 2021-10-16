package com.gexingw.shop.constant;

public interface ProductConstant {
    public final String REDIS_PRODUCT_FORMAT = "product:%d";

    // 商品分类的树形结构
    public final String REDIS_PRODUCT_CATEGORY_TREE = "product:category_tree";

    // 商品分类Redis缓存
    public final String REDIS_PRODUCT_CATEGORY_FORMAT = "product:category:%d";

    // 商品属性组 redis缓存
    public final String REDIS_PRODUCT_ATTRIBUTE_GROUP_FORMAT = "product:attribute-group:%d";
}
