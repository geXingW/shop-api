package com.gexingw.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.bean.pms.PmsProductCategory;
import com.gexingw.shop.dto.product.PmsProductCategoryRequestParam;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface PmsProductCategoryService {
    IPage<PmsProductCategory> searchList(QueryWrapper<PmsProductCategory> queryWrapper, IPage<PmsProductCategory> page);

    boolean incrParentCategorySubCnt(Long pid);

    boolean decrParentCategorySubCnt(Long pid);

    Long save(PmsProductCategoryRequestParam requestParam);

    PmsProductCategory getById(Long id);

    boolean update(PmsProductCategoryRequestParam requestParam);

    boolean delete(Set<Long> ids);

    List<PmsProductCategory> getByIds(Set<Long> ids);
}
