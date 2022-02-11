package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.mapper.pms.PmsProductCategoryMapper;
import com.gexingw.shop.modules.pms.service.PmsProductCategoryService;
import com.gexingw.shop.modules.pms.vo.ProductCategoryTreeVO;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    PmsProductCategoryMapper categoryMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<ProductCategoryTreeVO> getCategoryTree() {
        List<ProductCategoryTreeVO> categoryTreeVOS = getCategoryTreeFromRedis();
        if (categoryTreeVOS != null) { // 尝试从Redis获取
            return categoryTreeVOS;
        }

        // 从DB获取
        categoryTreeVOS = getCategoryTreeByPid(0L);

        // 重新写入redis
        setCategoryTreeToRedis(categoryTreeVOS);

        return categoryTreeVOS;
    }

    @Override
    public List<ProductCategoryTreeVO> getCategoryTreeByPid(Long pid) {
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<PmsProductCategory>().eq("pid", pid);
        List<PmsProductCategory> _productCategories = categoryMapper.selectList(queryWrapper);

        ArrayList<ProductCategoryTreeVO> productCategoryTreeVOs = new ArrayList<>();
        for (PmsProductCategory _productCategory : _productCategories) {
            // 取BO赋值到VO
            ProductCategoryTreeVO productCategoryTreeVO = new ProductCategoryTreeVO();
            productCategoryTreeVO.setId(_productCategory.getId());
            productCategoryTreeVO.setName(_productCategory.getName());
            productCategoryTreeVO.setIcon(_productCategory.getIcon());

            // 获取子分类
            productCategoryTreeVO.setChildren(getCategoryTreeByPid(_productCategory.getId()));

            // 加入列表
            productCategoryTreeVOs.add(productCategoryTreeVO);
        }

        return productCategoryTreeVOs;
    }

    @Override
    public Boolean setCategoryTreeToRedis(List<ProductCategoryTreeVO> categoryTreeVOS) {
        String jsonString = JSON.toJSONString(categoryTreeVOS);
        return redisUtil.set(ProductConstant.REDIS_PRODUCT_CATEGORY_TREE, jsonString);
    }

    @Override
    public List<ProductCategoryTreeVO> getCategoryTreeFromRedis() {
        Object redisObj = redisUtil.get(ProductConstant.REDIS_PRODUCT_CATEGORY_TREE);
        if (redisObj == null) {
            return null;
        }

        TypeReference<List<ProductCategoryTreeVO>> reference = new TypeReference<List<ProductCategoryTreeVO>>() {
        };
        return JSONObject.parseObject(redisObj.toString(), reference);
    }
}
