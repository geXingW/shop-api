package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsProductSkuService {
    boolean save(String productId, List<PmsProductRequestParam.Sku> skuList);

    boolean delProductAttributesByPid(String productId);

    List<PmsProductSku> getSkuListByProductId(String productId);

    boolean batchDelProductSkuByProductIds(Set<String> ids);
}
