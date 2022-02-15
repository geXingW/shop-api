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

    boolean update(PmsProduct product);

    PmsProduct getById(Long id);

    boolean delete(Set<Long> ids);

    PmsProduct getRedisProductByProductId(Long productId);

    boolean setRedisProductByProductId(Long productId, PmsProduct product);

    void delRedisProductByProductId(Long productId);
}
