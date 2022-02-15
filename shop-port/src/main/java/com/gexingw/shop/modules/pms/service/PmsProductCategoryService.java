package com.gexingw.shop.modules.pms.service;

import com.gexingw.shop.modules.pms.vo.PmsProductCategoryTreeVO;

import java.util.List;

public interface PmsProductCategoryService {
    List<PmsProductCategoryTreeVO> getCategoryTree();

    List<PmsProductCategoryTreeVO> getCategoryTreeByPid(Long pid);

    Boolean setCategoryTreeToRedis(List<PmsProductCategoryTreeVO> categoryTreeVOS);

    List<PmsProductCategoryTreeVO> getCategoryTreeFromRedis();
}
