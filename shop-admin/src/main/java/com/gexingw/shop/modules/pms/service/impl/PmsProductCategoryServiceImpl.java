package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.mapper.pms.PmsProductCategoryMapper;
import com.gexingw.shop.mapper.sys.SysUploadMapper;
import com.gexingw.shop.modules.pms.dto.category.PmsProductCategoryRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductCategoryService;
import com.gexingw.shop.modules.pms.vo.category.PmsProductCategoryAttributeVO;
import com.gexingw.shop.modules.pms.vo.category.ProductCategoryTreeVO;
import com.gexingw.shop.modules.sys.service.UploadService;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    PmsProductCategoryMapper categoryMapper;

    @Autowired
    SysUploadMapper sysUploadMapper;

    @Autowired
    UploadService uploadService;

    @Autowired
    RedisUtil redisUtil;

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
        // ???????????????Redis??????
        PmsProductCategory productCategory = getProductCategoryFromRedisById(id);
        if (productCategory != null) {
            return productCategory;
        }

        // ???DB??????
        productCategory = categoryMapper.selectById(id);

        // ????????????redis
        setProductCategoryRedisById(id, productCategory);

        return productCategory;
    }

    public PmsProductCategory getProductCategoryFromRedisById(Long id) {
        Object redisObj = redisUtil.get(String.format(ProductConstant.REDIS_PRODUCT_CATEGORY_FORMAT, id));
        if (redisObj == null) {
            return null;
        }

        return JSONObject.parseObject(redisObj.toString(), PmsProductCategory.class);
    }

    public Boolean setProductCategoryRedisById(Long id, PmsProductCategory productCategory) {
        return redisUtil.set(String.format(ProductConstant.REDIS_PRODUCT_CATEGORY_FORMAT, id), productCategory);
    }

    public void delProductCategoryRedisById(Long id) {
        redisUtil.del(String.format(ProductConstant.REDIS_PRODUCT_CATEGORY_FORMAT, id));
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
    public List<PmsProductCategory> getByProductIds(Set<String> productIds) {
        if (productIds.size() == 0) {
            return new ArrayList<>();
        }

        return categoryMapper.selectList(new QueryWrapper<PmsProductCategory>().in("pid", productIds));
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

    @Override
    public List<Map<String, Object>> getByPid(long pid) {
        ArrayList<Map<String, Object>> results = new ArrayList<>();

        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<PmsProductCategory>().eq("pid", pid).orderByAsc("sort");
        List<PmsProductCategory> categories = categoryMapper.selectList(queryWrapper);
        for (PmsProductCategory category : categories) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", category.getId());
            map.put("pid", category.getPid());
            map.put("name", category.getName());
            map.put("label", category.getName());
            map.put("subCount", category.getSubCount());
            map.put("hasChildren", category.isHasChildren());

            if (category.isHasChildren()) { // ????????????????????????????????????????????????
                map.put("children", new ArrayList<>());
            }

            results.add(map);
        }

        return results;
    }

    @Override
    public List<ProductCategoryTreeVO> getCategoryTree() {
        List<ProductCategoryTreeVO> categoryTreeVOS = getCategoryTreeFromRedis();
        if (categoryTreeVOS != null) { // ?????????Redis??????
            return categoryTreeVOS;
        }

        // ???DB??????
        categoryTreeVOS = getCategoryTreeByPid(0L);

        // ????????????redis
        setCategoryTreeToRedis(categoryTreeVOS);

        return categoryTreeVOS;
    }

    @Override
    public List<ProductCategoryTreeVO> getCategoryTreeByPid(Long pid) {
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<PmsProductCategory>().eq("pid", pid);
        List<PmsProductCategory> _productCategories = categoryMapper.selectList(queryWrapper);

        ArrayList<ProductCategoryTreeVO> productCategoryTreeVOs = new ArrayList<>();
        for (PmsProductCategory _productCategory : _productCategories) {
            // ???BO?????????VO
            ProductCategoryTreeVO productCategoryTreeVO = new ProductCategoryTreeVO();
            productCategoryTreeVO.setId(_productCategory.getId());
            productCategoryTreeVO.setName(_productCategory.getName());
            productCategoryTreeVO.setIcon(_productCategory.getIcon());

            // ???????????????
            productCategoryTreeVO.setChildren(getCategoryTreeByPid(_productCategory.getId()));

            // ????????????
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

    @Override
    public void delCategoryTreeFromRedis() {
        redisUtil.del(ProductConstant.REDIS_PRODUCT_CATEGORY_TREE);
    }

    @Override
    public PmsProductCategoryAttributeVO.Attribute getFormatCategoryAttributeVO(PmsProductAttribute saleAttribute) {
        PmsProductCategoryAttributeVO.Attribute attribute = new PmsProductCategoryAttributeVO.Attribute();
        attribute.setAttributeId(saleAttribute.getId());
        attribute.setAttributeName(saleAttribute.getName());
        attribute.setAttributeType(saleAttribute.getType());
        attribute.setInputType(saleAttribute.getInputType());
        attribute.setInputValue(saleAttribute.getInputValue());

        return attribute;
    }
}
