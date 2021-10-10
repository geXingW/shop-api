package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.service.PmsProductService;
import com.gexingw.shop.service.pms.PmsProductAttributeValueService;
import com.gexingw.shop.service.pms.PmsProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PmsProductServiceImpl implements PmsProductService {
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
        if(!attributeValueService.delProductAttributesByPid(productId)){
            throw new DBOperationException("原商品基本属性删除失败！");
        }

        // 保存新的商品基本属性
        if(!attributeValueService.save(productId, requestParam.getAttributeList())){
            throw new DBOperationException("商品基本属性保存失败！");
        }

        // 删除商品Sku信息
        if(!productSkuService.delProductAttributesByPid(productId)){
            throw new DBOperationException("原商品Sku信息删除失败！");
        }

        // 保存商品Sku信息
        if(!productSkuService.save(productId, requestParam.getSkuList())){
            throw new DBOperationException("商品Sku信息保存失败！");
        }

        return true;
    }

    @Override
    public PmsProduct getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public boolean delete(Set<Long> ids) {
        return productMapper.deleteBatchIds(ids) > 0;
    }

}
