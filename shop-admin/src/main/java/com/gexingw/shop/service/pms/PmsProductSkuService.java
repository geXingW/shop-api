package com.gexingw.shop.service.pms;

import com.gexingw.shop.dto.product.PmsProductRequestParam;

import java.util.List;

public interface PmsProductSkuService {
    boolean save(Long productId, List<PmsProductRequestParam.Sku> skuList);
}
