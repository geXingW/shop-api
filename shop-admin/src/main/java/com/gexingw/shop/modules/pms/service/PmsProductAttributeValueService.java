package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProductAttributeValue;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;

import java.util.List;
import java.util.Set;

public interface PmsProductAttributeValueService {
    boolean save(String productId, List<PmsProductRequestParam.Attribute> attributeList);

    List<PmsProductAttributeValue> getAttributeValuesByPid(String id);

    boolean delProductAttributesByPid(String productId);

    boolean updateAttributeNameByAttributeId(Long attributeId, String attributeName);

    boolean batchDelProductAttributesByProductIds(Set<String> ids);
}
