package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bo.Upload;
import com.gexingw.shop.bo.pms.PmsProductCategory;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.dto.product.PmsProductCategoryRequestParam;
import com.gexingw.shop.dto.product.PmsProductCategorySearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.PmsProductCategoryService;
import com.gexingw.shop.service.UploadService;
import com.gexingw.shop.service.impl.CommonServiceImpl;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("product/category")
public class ProductCategoryController {

    @Autowired
    PmsProductCategoryService categoryService;

    @Autowired
    CommonServiceImpl commonService;

    @Autowired
    UploadService uploadService;

    @GetMapping
    public R index(PmsProductCategorySearchParam searchParam) {
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<>();

        // 父级分类ID
        if (searchParam.getPid() != null) {
            queryWrapper.eq("pid", searchParam.getPid());
        }

        // 模糊查询
        if (searchParam.getBlurry() != null) {
            queryWrapper.and(q ->
                    q.like("name", searchParam.getBlurry())
                            .or()
                            .like("keywords", searchParam.getBlurry())
            );
        }

        // 创建时间
        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", searchParam.getCreateTimeBegin());
        }

        if (searchParam.getCreateTimeEnd() != null) {
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        // 设置排序字段
        queryWrapper.orderBy(true, searchParam.sortAsc(), searchParam.sortField());

        IPage<PmsProductCategory> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(categoryService.searchList(queryWrapper, page)));
    }

    @GetMapping("tree")
    public R tree() {
        // 查询顶级分类
        List<Map<String, Object>> categories = categoryService.getByPid(0L);
        for (Map<String, Object> category : categories) {
            boolean hasChildren = (boolean) category.get("hasChildren");
            if (!hasChildren) { // 没有子分类的话，无需查询子分类
                continue;
            }
            List<Map<String, Object>> children = categoryService.getByPid((Long) category.get("id"));
            category.put("children", children);
        }

        return R.ok(categories);
    }

    @PostMapping
    public R save(@RequestBody PmsProductCategoryRequestParam requestParam) {
        Long categoryId = categoryService.save(requestParam);
        if (categoryId == 0) {
            return R.ok(RespCode.FAILURE.getCode(), "保存失败！");
        }

        // 更新父级分类的子分类数量
        if (requestParam.getPid() != 0) {
            categoryService.incrParentCategorySubCnt(requestParam.getPid());
        }

        // 关联上传的图片
        Upload upload = uploadService.attachPicToSource(categoryId, UploadConstant.UPLOAD_TYPE_PRODUCT_CATEGORY, requestParam.getIcon());

        return upload != null ? R.ok("已保存！") : R.ok("保存失败！");
    }

    /**
     * 上传接口
     *
     * @return
     */
    @PostMapping("/upload-pic")
    public R upload(@RequestParam MultipartFile file, @RequestParam String uploadType, @RequestParam Long uploadId) {
        // 上传文件获取服务器文件路径
        File uploadedFile = commonService.upload(file, uploadType);
        if (uploadedFile == null) {
            return R.ok("上传失败！");
        }

        // 删除旧文件
        if (uploadId != 0) {
            if (!commonService.detachOldFile(uploadId, uploadType)) {
                return R.ok(RespCode.DELETE_FAILURE.getCode(), "旧图片删除失败！");
            }
        }

        // 绑定新的上传文件
        Upload upload = commonService.attachUploadFile(uploadId, uploadType, uploadedFile);
        if (upload == null) {
            return R.ok("上传失败！");
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("url", upload.getFullUrl());

        return R.ok(result, "上传成功！");
    }

    @PutMapping
    public R update(@RequestBody PmsProductCategoryRequestParam requestParam) {
        PmsProductCategory productCategory = categoryService.getById(requestParam.getId());
        if (productCategory == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品的分类不存在！");
        }

        // 更新分类信息
        if (!categoryService.update(requestParam)) {
            return R.ok(RespCode.FAILURE.getCode(), "更新失败！");
        }

        // 更新父级分类的sub count
        if (productCategory.getPid().equals(requestParam.getPid())) {
            return R.ok("已更新！");
        }

        if (!categoryService.decrParentCategorySubCnt(productCategory.getPid())) { // 减少旧父类
            return R.ok(RespCode.FAILURE, "更新父级分类失败！");
        }

        if (!categoryService.incrParentCategorySubCnt(requestParam.getPid())) {    // 增加新父类
            return R.ok(RespCode.FAILURE, "更新父级分类失败！");
        }

        // 更新分类图片
        Upload upload = uploadService.attachPicToSource(requestParam.getId(), UploadConstant.UPLOAD_TYPE_PRODUCT_CATEGORY, requestParam.getIcon());

        return upload != null ? R.ok("已更新！") : R.ok(RespCode.UPDATE_FAILURE.getCode(), "更新失败！");
    }

    @DeleteMapping
    public R delete(@RequestBody Set<Long> ids) {
        List<PmsProductCategory> categories = categoryService.getByIds(ids);

        if (!categoryService.delete(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }
        for (PmsProductCategory category : categories) {
            categoryService.decrParentCategorySubCnt(category.getPid());
        }

        // 删除关联的图片
        for (Long id : ids) {
            uploadService.detachSourcePic(id, UploadConstant.UPLOAD_TYPE_PRODUCT_CATEGORY);
        }

        return R.ok("已更新！");
    }
}
