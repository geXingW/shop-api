package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bean.pms.PmsProduct;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface PmsProductService {
    IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> objectPage);

    Long save(PmsProductRequestParam requestParam);

    boolean update(PmsProductRequestParam requestParam);

    PmsProduct getById(Long id);

    boolean delete(Set<Long> ids);
}
