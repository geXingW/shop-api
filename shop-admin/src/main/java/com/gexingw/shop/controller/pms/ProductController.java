package com.gexingw.shop.controller.pms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.*;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.dto.product.PmsProductSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.PmsProductAttributeService;
import com.gexingw.shop.service.PmsProductCategoryService;
import com.gexingw.shop.service.PmsProductService;
import com.gexingw.shop.service.UploadService;
import com.gexingw.shop.service.pms.PmsProductAttributeValueService;
import com.gexingw.shop.service.pms.PmsProductSkuService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.vo.pms.PmsProductInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

        return R.ok(PageUtil.format(productService.search(queryWrapper, page)));
    }

    @GetMapping("/{id}")
    public R show(@PathVariable Long id) {
        PmsProduct product = productService.getById(id);
        if (product == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "未找到该商品信息！");
        }

        PmsProductInfoVO productInfoVO = new PmsProductInfoVO();
        productInfoVO.setProductInfo(product);

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
        productInfoVO.setSkuList(skuList);

        return R.ok(productInfoVO);
    }

    @PostMapping
    public R add(@Validated @RequestBody PmsProductRequestParam requestParam) {
        Long productId = productService.save(requestParam);
        if (productId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "添加失败！");
        }

        // 更新商品分类的商品数量
        if (!categoryService.incrProductCntByCategoryId(requestParam.getCategoryId())) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "商品分类商品数更新失败！");
        }

        // 将商品与图片进行绑定
        if (requestParam.getPic().isEmpty()) {
            return R.ok("已添加！");
        }

        SysUpload upload = uploadService.attachPicToSource(productId, UploadConstant.UPLOAD_TYPE_PRODUCT, requestParam.getPic());

        return upload != null ? R.ok("已添加！") : R.ok(RespCode.FAILURE.getCode(), "添加失败！");
    }

    @PutMapping("{id}")
    public R update(@PathVariable("id") Long productId, @RequestBody PmsProductRequestParam requestParam) {
        PmsProduct product = productService.getById(productId);
        if (product == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "未找到商品信息！");
        }

        // 更新商品信息
        if (!productService.update(productId, requestParam)) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
        }

        // 更新商品分类数量
        if (!product.getCategoryId().equals(requestParam.getCategoryId())) {
            // 更新商品分类商品数
            if (!categoryService.decrProductCntByCategoryId(product.getCategoryId())) {
                return R.ok(RespCode.FAILURE.getCode(), "商品分类商品数更新失败！");
            }

            // 增加新商品分类商品数
            categoryService.incrProductCntByCategoryId(requestParam.getCategoryId());
        }

        // 更新商品图片
        if (!product.getPic().equals(requestParam.getPic())) {
            // 删除旧图片
            uploadService.detachSourcePic(productId, UploadConstant.UPLOAD_TYPE_PRODUCT);

            // 绑定新图片
            uploadService.attachPicToSource(productId, UploadConstant.UPLOAD_TYPE_PRODUCT, requestParam.getPic());
        }

        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        List<PmsProductCategory> categories = categoryService.getByIds(ids);
        if (!productService.delete(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        for (PmsProductCategory category : categories) {
            categoryService.decrProductCntByCategoryId(category.getPid());
        }

        // 删除管理图片
        for (Long id : ids) {
            uploadService.detachSourcePic(id, UploadConstant.UPLOAD_TYPE_PRODUCT);
        }

        return R.ok("已删除！");
    }
}
