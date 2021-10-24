package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeValueService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsProductMapper productMapper;

    @Autowired
    PmsProductAttributeValueService attributeValueService;

    @Autowired
    PmsProductSkuService productSkuService;

    @Override
    public IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> page) {
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PmsProductRequestParam requestParam) {
        PmsProduct product = new PmsProduct();

        BeanUtils.copyProperties(requestParam, product);
        product.setAlbumPics(requestParam.getPics());   // 相册

        if (productMapper.insert(product) <= 0) {
            return null;
        }

        Long productId = Long.valueOf(product.getId());   // 新的商品ID
        if (!attributeValueService.save(productId, requestParam.getAttributeList())) {
            throw new DBOperationException("商品基本属性保存失败!");
        }

        // 保存商品Sku信息
        if (!productSkuService.save(productId, requestParam.getSkuList())) {
            throw new DBOperationException("商品Sku信息保存失败！");
        }

        return productId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Long productId, PmsProductRequestParam requestParam) {
        PmsProduct product = productMapper.selectById(productId);
        if (product == null) {
            return false;
        }

        BeanUtils.copyProperties(requestParam, product);
        if (productMapper.updateById(product) <= 0) {
            throw new DBOperationException("商品信息保存失败！");
        }

        // 删除原有商品基本属性
        if (!attributeValueService.delProductAttributesByPid(productId)) {
            throw new DBOperationException("原商品基本属性删除失败！");
        }

        // 保存新的商品基本属性
        if (!attributeValueService.save(productId, requestParam.getAttributeList())) {
            throw new DBOperationException("商品基本属性保存失败！");
        }

        // 删除商品Sku信息
        if (!productSkuService.delProductAttributesByPid(productId)) {
            throw new DBOperationException("原商品Sku信息删除失败！");
        }

        // 保存商品Sku信息
        if (!productSkuService.save(productId, requestParam.getSkuList())) {
            throw new DBOperationException("商品Sku信息保存失败！");
        }

        return true;
    }

    @Override
    public PmsProduct getById(Long id) {
        // 尝试从Redis获取
        PmsProduct product = this.getRedisProductByProductId(id);
        if (product != null) {
            return product;
        }

        // 从DB获取
        product = productMapper.selectById(id);

        // 重新写入redis
        if (product != null) {
            this.setRedisProductByProductId(id, product);
        }

        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Set<Long> ids) {
        // 删除商品关联的属性信息
        if (!attributeValueService.batchDelProductAttributesByProductIds(ids)) {
            throw new DBOperationException("商品属性删除失败！");
        }

        // 删除商品Sku信息
        if (!productSkuService.batchDelProductSkuByProductIds(ids)) {
            throw new DBOperationException("商品sku信息删除失败！");
        }

        if (productMapper.deleteBatchIds(ids) <= 0) {
            throw new DBOperationException("商品信息删除失败！");
        }

        return true;
    }

    private PmsProduct getRedisProductByProductId(Long productId) {
        Object redisObj = redisUtil.get(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), PmsProduct.class);
    }

    private boolean setRedisProductByProductId(Long productId, PmsProduct product) {
        return redisUtil.set(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId), product);
    }

    private void delRedisProductByProductId(Long productId) {
        redisUtil.del(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
    }

}
