package com.gexingw.shop.service.pms;

import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.dto.product.PmsProductRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsProductSkuService {
    boolean save(Long productId, List<PmsProductRequestParam.Sku> skuList);

    boolean delProductAttributesByPid(Long productId);

    List<PmsProductSku> getSkuListByProductId(Long productId);

    boolean batchDelProductSkuByProductIds(Set<Long> ids);
}
