package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.modules.pms.vo.ProductCategoryTreeVO;

import java.util.List;

public interface PmsProductCategoryService {
    List<ProductCategoryTreeVO> getCategoryTree();

    List<ProductCategoryTreeVO> getCategoryTreeByPid(Long pid);

    Boolean setCategoryTreeToRedis(List<ProductCategoryTreeVO> categoryTreeVOS);

    List<ProductCategoryTreeVO> getCategoryTreeFromRedis();
}
