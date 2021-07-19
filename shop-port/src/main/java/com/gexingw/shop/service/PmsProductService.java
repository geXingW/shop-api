package com.gexingw.shop.service;

import com.gexingw.shop.bo.pms.PmsProduct;

public interface PmsProductService {
    boolean isExist(Long id);

    PmsProduct getById(Long id);
}
