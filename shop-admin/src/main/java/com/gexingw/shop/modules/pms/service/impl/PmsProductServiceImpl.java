package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeValueService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.utils.RedisUtil;
import com.gexingw.shop.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

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

    @Autowired
    FileConfig fileConfig;

    @Override
    public IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> page) {
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PmsProductRequestParam requestParam) {
        PmsProduct product = new PmsProduct();

        BeanUtils.copyProperties(requestParam, product);

        // 处理商品中带的域名
        this.removeProductImageDomain(product, requestParam);

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

        // 处理商品中带的域名
        this.removeProductImageDomain(product, requestParam);

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
    public boolean update(PmsProduct product) {
        return productMapper.updateById(product) >= 0;
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

    @Override
    public PmsProduct getRedisProductByProductId(Long productId) {
        Object redisObj = redisUtil.get(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), PmsProduct.class);
    }

    @Override
    public boolean setRedisProductByProductId(Long productId, PmsProduct product) {
        return redisUtil.set(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId), product);
    }

    @Override
    public void delRedisProductByProductId(Long productId) {
        redisUtil.del(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
    }

    private void removeProductImageDomain(PmsProduct product, PmsProductRequestParam requestParam) {
        // 商品图片
        List<String> picsWithoutDomain = requestParam.getPicsWithoutDomain(fileConfig);
        product.setAlbumPics(String.join(",", picsWithoutDomain));   // 相册
        product.setPic(picsWithoutDomain.get(0)); //主图

        // 商品详情
        String separator = Matcher.quoteReplacement(File.separator);
        String fileDomain = StringUtil.trim(fileConfig.getDiskHost(), separator) + separator;
        product.setDetailPCHtml(requestParam.getDetailPCHtml().replaceAll("src=\"" + fileDomain, "src=\""));
        product.setDetailMobileHtml(requestParam.getDetailMobileHtml().replaceAll("src=\"" + fileDomain, "src=\""));
    }
}
