package com.gexingw.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.pms.PmsProduct;
import com.gexingw.shop.dto.product.PmsProductRequestParam;
import com.gexingw.shop.dto.product.PmsProductSearchParam;
import com.gexingw.shop.service.PmsProductService;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    PmsProductService productService;

    @GetMapping
    public R index(PmsProductSearchParam searchParam) {
        // 查询条件
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        // 分页条件
        IPage<PmsProduct> page = new Page<>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(productService.search(queryWrapper, page)));
    }

    @PostMapping
    public R add(@RequestBody PmsProductRequestParam requestParam){
        System.out.println(requestParam);
        return R.ok();
    }
}
