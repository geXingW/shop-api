package com.gexingw.shop.modules.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.pms.PmsProductAttribute;
import com.gexingw.shop.bo.pms.PmsProductAttributeGroup;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.enums.PmsProductAttributeTypeEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.category.PmsProductCategoryRequestParam;
import com.gexingw.shop.modules.pms.dto.category.PmsProductCategorySearchParam;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeGroupService;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeService;
import com.gexingw.shop.modules.pms.service.PmsProductCategoryService;
import com.gexingw.shop.modules.pms.vo.category.PmsProductCategoryAttributeVO;
import com.gexingw.shop.modules.sys.service.UploadService;
import com.gexingw.shop.modules.sys.service.impl.CommonServiceImpl;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product/category")
public class ProductCategoryController {

    @Autowired
    PmsProductCategoryService categoryService;

    @Autowired
    PmsProductAttributeGroupService attributeGroupService;

    @Autowired
    PmsProductAttributeService attributeService;

    @Autowired
    CommonServiceImpl commonService;

    @Autowired
    UploadService uploadService;

    @GetMapping
    public R index(PmsProductCategorySearchParam searchParam) {
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<>();

        // ????????????ID
        if (searchParam.getPid() != null) {
            queryWrapper.eq("pid", searchParam.getPid());
        }

        // ????????????
        if (searchParam.getBlurry() != null) {
            queryWrapper.and(q ->
                    q.like("name", searchParam.getBlurry())
                            .or()
                            .like("keywords", searchParam.getBlurry())
            );
        }

        // ????????????
        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", searchParam.getCreateTimeBegin());
        }

        if (searchParam.getCreateTimeEnd() != null) {
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        // ??????????????????
        queryWrapper.orderBy(true, searchParam.sortAsc(), searchParam.sortField());

        IPage<PmsProductCategory> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(categoryService.searchList(queryWrapper, page)));
    }

    @GetMapping("tree")
    public R tree() {
        return R.ok(categoryService.getCategoryTree());
    }

    @PostMapping
    public R save(@RequestBody PmsProductCategoryRequestParam requestParam) {
        Long categoryId = categoryService.save(requestParam);
        if (categoryId == 0) {
            return R.failure(RespCode.SAVE_FAILURE);
        }

        // ????????????????????????????????????
        if (requestParam.getPid() != 0) {
            categoryService.incrParentCategorySubCnt(requestParam.getPid());
        }

        // ????????????????????????
        categoryService.delCategoryTreeFromRedis();

        // ????????????????????????????????????
        if (requestParam.getIcon() == null || requestParam.getIcon().isEmpty()) {
            return R.ok(RespCode.PRODUCT_CATEGORY_CREATED);
        }

        // ?????????????????????
        SysUpload upload = uploadService.attachPicToSource(categoryId, UploadConstant.UPLOAD_MODULE_PRODUCT_CATEGORY, UploadConstant.UPLOAD_TYPE_IMAGE, requestParam.getIcon());

        return upload != null ? R.ok(RespCode.PRODUCT_CREATED) : R.failure(RespCode.SAVE_FAILURE);
    }

    /**
     * ????????????
     *
     * @return
     */
    @PostMapping("/upload-pic")
    public R upload(@RequestParam MultipartFile file,
                    @RequestParam String uploadType,
                    @RequestParam Long uploadId,
                    @RequestParam String uploadModule) {
        // ???????????????????????????????????????
        File uploadedFile = commonService.upload(file, uploadType, uploadModule);
        if (uploadedFile == null) {
            return R.ok(RespCode.UPLOADED);
        }

        // ???????????????
        if (uploadId != 0) {
            if (!commonService.detachOldFile(uploadId, uploadType)) {
                return R.failure(RespCode.FILE_DELETE_FAILURE);
            }
        }

        // ????????????????????????
        SysUpload upload = commonService.attachUploadFile(uploadId, uploadType, uploadedFile);
        if (upload == null) {
            return R.failure(RespCode.UPLOAD_FAILURE);
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("url", upload.getFullUrl());

        return R.ok(RespCode.UPLOADED, result);
    }

    @PutMapping
    public R update(@RequestBody PmsProductCategoryRequestParam requestParam) {
        PmsProductCategory productCategory = categoryService.getById(requestParam.getId());
        if (productCategory == null) {
            return R.failure(RespCode.PRODUCT_CATEGORY_NOT_EXIST);
        }

        // ??????????????????
        if (!categoryService.update(requestParam)) {
            return R.failure(RespCode.UPDATE_FAILURE);
        }

        // ????????????????????????
        categoryService.delCategoryTreeFromRedis();

        // ?????????????????????sub count
        if (productCategory.getPid().equals(requestParam.getPid())) {
            return R.ok(RespCode.PRODUCT_CATEGORY_UPDATED);
        }

        // ???????????????
        if (!categoryService.decrParentCategorySubCnt(productCategory.getPid())) {
            return R.failure(RespCode.UPDATE_FAILURE, "???????????????????????????");
        }

        // ???????????????
        if (!categoryService.incrParentCategorySubCnt(requestParam.getPid())) {
            return R.failure(RespCode.UPDATE_FAILURE, "???????????????????????????");
        }

        if (requestParam.getIcon() == null || requestParam.getIcon().isEmpty()) {
            return R.ok(RespCode.PRODUCT_CATEGORY_UPDATED);
        }

        // ??????????????????
        SysUpload upload = uploadService.attachPicToSource(requestParam.getId(), UploadConstant.UPLOAD_MODULE_PRODUCT_CATEGORY, UploadConstant.UPLOAD_TYPE_IMAGE, requestParam.getIcon());

        return upload != null ? R.ok(RespCode.PRODUCT_CATEGORY_UPDATED) : R.failure(RespCode.UPDATE_FAILURE);
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        List<PmsProductCategory> categories = categoryService.getByIds(ids);

        if (!categoryService.delete(ids)) {
            return R.failure(RespCode.DELETE_FAILURE);
        }
        for (PmsProductCategory category : categories) {
            categoryService.decrParentCategorySubCnt(category.getPid());
        }

        // ????????????????????????
        categoryService.delCategoryTreeFromRedis();

        // ?????????????????????
        for (Long id : ids) {
            uploadService.detachSourcePic(id, UploadConstant.UPLOAD_MODULE_PRODUCT_CATEGORY);
        }

        return R.ok(RespCode.DELETE_FAILURE);
    }

    @GetMapping("attribute")
    public R attribute(PmsProductCategorySearchParam searchParam) {
        PmsProductCategory productCategory = categoryService.getById(searchParam.getCategoryId());
        if (productCategory == null) {
            return R.failure(RespCode.PRODUCT_CATEGORY_NOT_EXIST);
        }

        PmsProductCategoryAttributeVO productCategoryAttributeVO = new PmsProductCategoryAttributeVO();
        productCategoryAttributeVO.setCategoryId(productCategory.getId());
        productCategoryAttributeVO.setCategoryName(productCategory.getName());

        List<PmsProductAttributeGroup> attributeGroups = attributeGroupService.getAttributeGroupsByCategoryId(searchParam.getCategoryId());
        List<Long> attributeGroupIds = attributeGroups.stream().map(PmsProductAttributeGroup::getId).collect(Collectors.toList());

        // ????????????????????????????????????????????????????????????????????????????????????
        if (attributeGroupIds.size() == 0) {
            return R.ok(productCategoryAttributeVO);
        }

        List<Long> attachAttributeIds = attributeGroupService.getAttachAttributeIdsByGroupIds(attributeGroupIds);

        if (attachAttributeIds.size() > 0) {
            // ????????????
            List<PmsProductAttribute> attachedBaseAttributes = attributeService.getAttributesByTypeAndIds(PmsProductAttributeTypeEnum.BASE_ATTRIBUTE.getCode(), attachAttributeIds);
            for (PmsProductAttribute attachedBaseAttribute : attachedBaseAttributes) {
                productCategoryAttributeVO.baseAttributes.add(categoryService.getFormatCategoryAttributeVO(attachedBaseAttribute));
            }

            // ????????????
            List<PmsProductAttribute> attachedSaleAttributes = attributeService.getAttributesByTypeAndIds(PmsProductAttributeTypeEnum.SALE_ATTRIBUTE.getCode(), attachAttributeIds);
            for (PmsProductAttribute attachedSaleAttribute : attachedSaleAttributes) {
                productCategoryAttributeVO.saleAttributes.add(categoryService.getFormatCategoryAttributeVO(attachedSaleAttribute));
            }
        }
//
//        for (PmsProductAttributeGroup attributeGroup : attributeGroups) {
//            PmsProductCategoryAttributeVO.Group group = new PmsProductCategoryAttributeVO.Group(attributeGroup.getId(), attributeGroup.getName());
//
//            // ????????????
//            List<PmsProductAttribute> baseAttributes = attributeService.getAttributesByTypeAndIds(PmsProductAttributeTypeEnum.BASE_ATTRIBUTE.getCode(), attachAttributeIds);
//            for (PmsProductAttribute baseAttribute : baseAttributes) {
//                group.baseAttributes.add(categoryService.getFormatCategoryAttributeVO(baseAttribute));
//            }
//
//            // ????????????
//            List<PmsProductAttribute> saleAttributes = attributeService.getAttributesByTypeAndIds(PmsProductAttributeTypeEnum.SALE_ATTRIBUTE.getCode(), attachAttributeIds);
//            for (PmsProductAttribute saleAttribute : saleAttributes) {
//                group.saleAttributes.add(categoryService.getFormatCategoryAttributeVO(saleAttribute));
//            }
//
//            productCategoryAttributeVO.groups.add(group);
//        }

        return R.ok(productCategoryAttributeVO);
    }
}
