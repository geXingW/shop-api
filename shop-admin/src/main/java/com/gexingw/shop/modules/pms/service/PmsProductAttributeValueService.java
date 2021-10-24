package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProductAttributeValue;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsProductAttributeValueService {
    boolean save(Long productId, List<PmsProductRequestParam.Attribute> attributeList);

    List<PmsProductAttributeValue> getAttributeValuesByPid(Long id);

    boolean delProductAttributesByPid(Long productId);

    boolean updateAttributeNameByAttributeId(Long attributeId, String attributeName);

    boolean batchDelProductAttributesByProductIds(Set<Long> ids);
}
