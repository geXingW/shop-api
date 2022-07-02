package com.gexingw.shop.modules.pms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.*;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchParam;
import com.gexingw.shop.modules.pms.service.*;
import com.gexingw.shop.modules.pms.vo.product.PmsProductInfoVO;
import com.gexingw.shop.modules.sys.service.UploadService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    PmsProductService productService;

    @Autowired
    PmsProductCategoryService categoryService;

    @Autowired
    PmsProductAttributeService attributeService;

    @Autowired
    PmsProductAttributeValueService attributeValueService;

    @Autowired
    PmsProductSkuService skuService;

    @Autowired
    UploadService uploadService;

    @Autowired
    FileConfig fileConfig;

    @GetMapping
    public R index(PmsProductSearchParam searchParam) {
        // 查询条件
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        if (searchParam.getBlurry() != null) {
            queryWrapper.and(q -> q.like("title", searchParam.getBlurry()).or().like("sub_title", searchParam.getBlurry()));
        }

        // 添加时间
        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", searchParam.getCreateTimeBegin());
        }

        if (searchParam.getCreateTimeEnd() != null) {
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        // 新品推荐
        if (searchParam.getIsNew() != null) {
            queryWrapper.eq("is_new", searchParam.getIsNew());
        }

        // 商品推荐
        if (searchParam.getIsRecommend() != null) {
            queryWrapper.eq("is_recommend", searchParam.getIsRecommend());
        }

        // 分页条件
        IPage<PmsProduct> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        IPage<PmsProduct> searchPage = productService.search(queryWrapper, page);

        // 绑定商品分类信息
        List<PmsProduct> searchRecords = searchPage.getRecords();

        Map<String, Object> result = PageUtil.format(searchPage);

        List<PmsProductInfoVO> records = new ArrayList<>();
        for (PmsProduct product : searchRecords) {
            PmsProductInfoVO productInfoVO = new PmsProductInfoVO().setProductInfo(product).setProductPics(product, fileConfig);

            // 商品分类
            PmsProductCategory category = categoryService.getById(product.getCategoryId());
            productInfoVO.setCategoryName(category.getName());

            records.add(productInfoVO);
        }

        result.put("records", records);

        return R.ok(result);
    }

    @GetMapping("/{id}")
    public R show(@PathVariable String id) {
        PmsProduct product = productService.getById(id);
        if (product == null) {
            return R.failure(RespCode.PRODUCT_NOT_EXIST);
        }

        PmsProductInfoVO productInfoVO = new PmsProductInfoVO();
        productInfoVO.setProductInfo(product);

        // 商品分类
        PmsProductCategory category = categoryService.getById(product.getCategoryId());
        if (category != null) {
            productInfoVO.setCategoryName(category.getName());
        }

        // 属性值
        List<PmsProductAttributeValue> attributeValues = attributeValueService.getAttributeValuesByPid(id);

        // 根据所有的属性值获取属性属性信息
        List<Long> attributeIds = attributeValues.stream().map(PmsProductAttributeValue::getProductAttributeId).collect(Collectors.toList());
        Map<Long, PmsProductAttribute> attributeMap = attributeService.getAttrsMapKeyByAttrIdByAttrIds(attributeIds);

        ArrayList<PmsProductInfoVO.AttributeItem> attributeList = new ArrayList<>();
        for (PmsProductAttributeValue attributeValue : attributeValues) {
            PmsProductInfoVO.AttributeItem attributeItem = new PmsProductInfoVO.AttributeItem();
            attributeItem.setId(attributeValue.getProductAttributeId());
            attributeItem.setValue(attributeValue.getProductAttributeValue());

            String attributeName = attributeMap.get(attributeValue.getProductAttributeId()) != null ?
                    attributeMap.get(attributeValue.getProductAttributeId()).getName() : "";
            attributeItem.setName(attributeName);

            attributeList.add(attributeItem);
        }
        productInfoVO.setAttributeList(attributeList);

        // 商品Sku
        ArrayList<PmsProductInfoVO.SkuListItem> skuList = new ArrayList<>();
        List<PmsProductSku> productSkuList = skuService.getSkuListByProductId(id);

        TypeReference<List<PmsProductInfoVO.SkuItem>> reference = new TypeReference<List<PmsProductInfoVO.SkuItem>>() {
        };
        for (PmsProductSku productSku : productSkuList) {
            PmsProductInfoVO.SkuListItem skuItem = new PmsProductInfoVO.SkuListItem();
            skuItem.setPrice(productSku.getPrice());
            skuItem.setStock(productSku.getStock());
            skuItem.setLowStock(productSku.getLowStock());
            skuItem.setSku(JSON.parseObject(productSku.getSpData(), reference));

            skuList.add(skuItem);
        }
        productInfoVO.setProductInfo(product).setProductPics(product, fileConfig).setSkuList(skuList);

        return R.ok(productInfoVO);
    }

    @PostMapping
    public R add(@Validated @RequestBody PmsProductRequestParam requestParam) {
        String productId = productService.save(requestParam);
        if (productId == null) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        // 更新商品分类的商品数量
        if (!categoryService.incrProductCntByCategoryId(requestParam.getCategoryId())) {
            return R.failure(RespCode.UPLOAD_FAILURE);
        }

        // 商品图片
        List<String> pics = requestParam.getPics();

        // 将商品与图片进行绑定
        if (pics.isEmpty()) {
            return R.ok(RespCode.PRODUCT_UPDATED);
        }

        for (String pic : pics) {
            SysUpload upload = uploadService.attachPicToSource(Long.valueOf(productId), UploadConstant.UPLOAD_MODULE_PRODUCT, UploadConstant.UPLOAD_TYPE_IMAGE, pic);
            if (upload == null) {
                return R.failure(RespCode.UPLOAD_FAILURE);
            }
        }

        return R.ok(RespCode.PRODUCT_UPDATED);
    }

    @PutMapping("{id}")
    public R update(@PathVariable("id") String productId, @RequestBody PmsProductRequestParam requestParam) {
        PmsProduct product = productService.getById(productId);
        if (product == null) {
            return R.failure(RespCode.PRODUCT_NOT_EXIST);
        }

        // 更新商品信息
        if (!productService.update(productId, requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // 更新商品分类数量
        if (!product.getCategoryId().equals(requestParam.getCategoryId())) {
            // 更新商品分类商品数
            if (!categoryService.decrProductCntByCategoryId(product.getCategoryId())) {
                return R.failure(RespCode.UPDATE_FAILURE);
            }

            // 增加新商品分类商品数
            categoryService.incrProductCntByCategoryId(requestParam.getCategoryId());
        }

        // 清除商品redis缓存
        productService.delRedisProductByProductId(productId);

        try {
            // 删除ES信息
            if (!productService.delProductFromESById(productId)) {
                return R.failure(RespCode.ES_DELETE_FAILURE);
            }

            // 如果商品是上架状态，需要将旧的ES信息删除，并写入新的ES信息
            if (ProductConstant.ON_SALE.equals(product.getOnSale())) { // 上架
                product = productService.getById(productId);
                if (!productService.addProductToES(product)) {
                    return R.failure(RespCode.ES_SAVE_FAILURE);
                }
            }
        } catch (IOException e) {
            return R.failure(RespCode.ES_OPERATION_FAILURE);
        }

        // 更新商品图片
//        if (!product.getPic().equals(requestParam.getPic())) {
//            // 删除旧图片
//            uploadService.detachSourcePic(productId, UploadConstant.UPLOAD_MODULE_PRODUCT);
//
//            // 绑定新图片
//            uploadService.attachPicToSource(productId, UploadConstant.UPLOAD_MODULE_PRODUCT, UploadConstant.UPLOAD_TYPE_IMAGE, requestParam.getPic());
//        }

        return R.ok(RespCode.PRODUCT_UPDATED);
    }

    @PutMapping("change-sale-status/{id}")
    public R changeSaleStatus(@PathVariable("id") String productId, @RequestBody PmsProductRequestParam requestParam) {
        PmsProduct product = productService.getById(productId);
        if (product == null) {
            return R.failure(RespCode.PRODUCT_NOT_EXIST);
        }

        product.setOnSale(requestParam.getOnSale());
        if (!productService.update(product)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // 如果商品上架，将商品写入ES
        if (ProductConstant.ON_SALE.equals(product.getOnSale())) {
            try {
                if (!productService.addProductToES(product)) {
                    return R.failure(RespCode.ES_OPERATION_FAILURE);
                }
            } catch (IOException e) {
                return R.failure(RespCode.ES_OPERATION_FAILURE);
            }
        }

        // 下架，移除ES商品
        if (ProductConstant.OFF_SALE.equals(product.getOnSale())) {
            try {
                if (!productService.delProductFromESById(product.getId())) {
                    return R.failure(RespCode.ES_OPERATION_FAILURE);
                }
            } catch (IOException e) {
                return R.failure(RespCode.ES_OPERATION_FAILURE);
            }
        }

        // 删除Redis缓存
        productService.delRedisProductByProductId(productId);

        return R.ok(RespCode.PRODUCT_UPDATED);
    }

    @DeleteMapping
    public R delete(@RequestBody Set<String> ids) {
        List<PmsProduct> products = productService.getProductsByIds(ids);

        // 删除商品数据
        if (!productService.delete(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }

        for (PmsProduct product : products) {
            // 删除ES商品
            try {
                if (!productService.delProductFromESById(product.getId())) {
                    return R.failure(RespCode.ES_OPERATION_FAILURE);
                }
            } catch (IOException e) {
                return R.failure(RespCode.ES_OPERATION_FAILURE);
            }

            // 删除关联图片
            uploadService.detachSourcePic(Long.valueOf(product.getId()), UploadConstant.UPLOAD_MODULE_PRODUCT);

            // 删除Redis缓存
            productService.delRedisProductByProductId(product.getId());
        }

        List<PmsProductCategory> categories = categoryService.getByProductIds(ids);
        for (PmsProductCategory category : categories) {
            categoryService.decrProductCntByCategoryId(category.getPid());
        }

        return R.ok(RespCode.PRODUCT_DELETED);
    }
}
