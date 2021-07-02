package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bean.pms.PmsProduct;
import com.gexingw.shop.dto.product.PmsProductRequestParam;

import java.util.List;

public interface PmsProductService {
    IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> objectPage);

    Long save(PmsProductRequestParam requestParam);
}
