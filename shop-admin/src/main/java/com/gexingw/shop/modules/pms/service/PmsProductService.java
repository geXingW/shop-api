package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;

import java.util.Set;

public interface PmsProductService {
    IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> objectPage);

    Long save(PmsProductRequestParam requestParam);

    boolean update(Long productId, PmsProductRequestParam requestParam);

    PmsProduct getById(Long id);

    boolean delete(Set<Long> ids);

}