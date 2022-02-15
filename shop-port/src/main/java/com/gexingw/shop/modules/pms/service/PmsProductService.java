package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;

import java.util.List;

public interface PmsProductService {
    boolean isExist(Long id);

    PmsProduct getById(String id);

    List<PmsProduct> getByIds(List<String> ids);

    boolean decrProductStockById(String id, Integer quantity);

    IPage<PmsProduct> search(PmsProductSearchRequestParam requestParam);
}
