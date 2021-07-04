package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.bean.pms.PmsProductCategory;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.dto.product.PmsProductCategoryRequestParam;
import com.gexingw.shop.mapper.PmsProductCategoryMapper;
import com.gexingw.shop.mapper.UploadMapper;
import com.gexingw.shop.service.PmsProductCategoryService;
import com.gexingw.shop.service.UploadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
public class PmsProductServiceCategoryImpl implements PmsProductCategoryService {

    @Autowired
    PmsProductCategoryMapper categoryMapper;

    @Autowired
    UploadMapper uploadMapper;

    @Autowired
    UploadService uploadService;

    @Override
    public IPage<PmsProductCategory> searchList(QueryWrapper<PmsProductCategory> queryWrapper, IPage<PmsProductCategory> page) {
        return categoryMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean incrParentCategorySubCnt(Long pid) {
        PmsProductCategory parentCategory = categoryMapper.selectById(pid);
        if (parentCategory == null) {
            return true;
        }

        parentCategory.incrSubCount(1);
        return categoryMapper.updateById(parentCategory) > 0;
    }

    public boolean decrParentCategorySubCnt(Long pid) {
        PmsProductCategory parentCategory = categoryMapper.selectById(pid);
        if (parentCategory == null) {
            return true;
        }

        parentCategory.decrSubCount(1);
        return categoryMapper.updateById(parentCategory) > 0;
    }

    @Override
    public Long save(PmsProductCategoryRequestParam requestParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        BeanUtils.copyProperties(requestParam, productCategory);

        if (categoryMapper.insert(productCategory) <= 0) {
            return 0L;
        }

        return productCategory.getId();
    }

    @Override
    public PmsProductCategory getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public boolean update(PmsProductCategoryRequestParam requestParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        BeanUtils.copyProperties(requestParam, productCategory);

        return categoryMapper.updateById(productCategory) > 0;
    }

    @Override
    public boolean delete(Set<Long> ids) {
        return categoryMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<PmsProductCategory> getByIds(Set<Long> ids) {
        return categoryMapper.selectBatchIds(ids);
    }

    @Override
    public boolean incrProductCntByCategoryId(Long categoryId) {
        PmsProductCategory category = categoryMapper.selectById(categoryId);
        category.incrProductCnt(1);

        return categoryMapper.updateById(category) >= 0;
    }

    @Override
    public boolean decrProductCntByCategoryId(Long categoryId) {
        PmsProductCategory category = categoryMapper.selectById(categoryId);
        category.decrProductCnt(1);

        return categoryMapper.updateById(category) >= 0;
    }
}
