package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PmsProductService {
    IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> objectPage);

    String save(PmsProductRequestParam requestParam);

    boolean update(String productId, PmsProductRequestParam requestParam);

    boolean update(PmsProduct product);

    PmsProduct getById(String id);

    boolean delete(Set<String> ids);

    PmsProduct getRedisProductByProductId(String productId);

    boolean setRedisProductByProductId(String productId, PmsProduct product);

    void delRedisProductByProductId(String productId);

    boolean addProductToES(PmsProduct product) throws IOException;

    boolean delProductFromESById(String id) throws IOException;

    List<PmsProduct> getProductsByIds(Set<String> ids);
}
