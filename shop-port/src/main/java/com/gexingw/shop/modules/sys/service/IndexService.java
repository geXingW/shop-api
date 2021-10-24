package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.pms.PmsBanner;
import com.gexingw.shop.bo.pms.PmsProduct;

import java.util.List;

public interface IndexService {
    List<PmsBanner> getBannerList();

    List<PmsProduct> getHotProduct();

    List<PmsProduct> getRecommendProduct();

    List<PmsProduct> getNewProduct();
}
