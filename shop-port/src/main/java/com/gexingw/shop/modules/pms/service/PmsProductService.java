package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.bo.pms.PmsProduct;

import java.util.List;

public interface PmsProductService {
    boolean isExist(Long id);

    PmsProduct getById(Long id);

    List<PmsProduct> getByIds(List<Long> ids);

    boolean decrProductStockById(Long id, Integer quantity);
}
