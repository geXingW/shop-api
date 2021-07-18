package com.gexingw.shop.service;

import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.bo.pms.PmsProduct;

import java.util.List;

public interface IndexService {
    List<PmsBanner> getBannerList();

    List<PmsProduct> getHotProduct();

    List<PmsProduct> getRecommendProduct();

    List<PmsProduct> getNewProduct();
}
