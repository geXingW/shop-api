package com.gexingw.shop.modules.pms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.modules.pms.dto.category.PmsProductCategoryRequestParam;
import com.gexingw.shop.modules.pms.vo.category.PmsProductCategoryAttributeVO;
import com.gexingw.shop.modules.pms.vo.category.ProductCategoryTreeVO;

import java.util.List;
import java.util.Map;
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

    List<PmsProductCategory> getByProductIds(Set<String> productIds);

    boolean incrProductCntByCategoryId(Long categoryId);

    boolean decrProductCntByCategoryId(Long categoryId);

    List<Map<String, Object>> getByPid(long pid);

    List<ProductCategoryTreeVO> getCategoryTreeByPid(Long pid);

    List<ProductCategoryTreeVO> getCategoryTree();

    Boolean setCategoryTreeToRedis(List<ProductCategoryTreeVO> categoryTreeVOS);

    List<ProductCategoryTreeVO> getCategoryTreeFromRedis();

    void delCategoryTreeFromRedis();

    PmsProductCategoryAttributeVO.Attribute getFormatCategoryAttributeVO(PmsProductAttribute saleAttribute);
}
