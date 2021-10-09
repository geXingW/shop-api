package com.gexingw.shop.service.pms;

import com.gexingw.shop.bo.pms.PmsProductAttributeValue;
import com.gexingw.shop.dto.product.PmsProductRequestParam;

import java.util.List;

public interface PmsProductAttributeValueService {
    boolean save(Long productId, List<PmsProductRequestParam.Attribute> attributeList);

    List<PmsProductAttributeValue> getAttributeValuesByPid(Long id);
}
