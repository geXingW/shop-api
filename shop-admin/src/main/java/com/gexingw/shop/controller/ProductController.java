package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.bean.pms.PmsProduct;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.dto.product.PmsProductSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.PmsProductCategoryService;
import com.gexingw.shop.service.PmsProductService;
import com.gexingw.shop.service.UploadService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    PmsProductService productService;

    @Autowired
    PmsProductCategoryService categoryService;

    @Autowired
    UploadService uploadService;

    @GetMapping
    public R index(PmsProductSearchParam searchParam) {
        // 查询条件
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        if (searchParam.getBlurry() != null) {
            queryWrapper.and(q -> q.like("title", searchParam.getBlurry()).or().like("sub_title", searchParam.getBlurry()));
        }

        if (searchParam.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", searchParam.getCreateTimeBegin());
        }

        if (searchParam.getCreateTimeEnd() != null) {
            queryWrapper.lt("create_time", searchParam.getCreateTimeEnd());
        }

        // 分页条件
        IPage<PmsProduct> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(productService.search(queryWrapper, page)));
    }

    @PostMapping
    public R add(@RequestBody PmsProductRequestParam requestParam) {
        Long productId = productService.save(requestParam);
        if (productId == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "添加失败！");
        }

        // 更新商品分类的商品数量
        if (!categoryService.incrProductCntByCategoryId(requestParam.getCategoryId())) {
            return R.ok(RespCode.UPDATE_FAILURE.getCode(), "商品分类商品数更新失败！");
        }

        // 将商品与图片进行绑定
        Upload upload = uploadService.attachPicToSource(productId, UploadConstant.UPLOAD_TYPE_PRODUCT, requestParam.getPic());

        return upload != null ? R.ok("已添加！") : R.ok(RespCode.FAILURE.getCode(), "添加失败！");
    }
}
