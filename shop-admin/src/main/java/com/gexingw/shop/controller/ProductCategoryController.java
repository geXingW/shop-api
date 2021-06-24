package com.gexingw.shop.controller;

import com.gexingw.shop.dto.product.PmsProductCategorySearchParam;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/category")
public class ProductCategoryController {

    @GetMapping
    public R index(@RequestParam PmsProductCategorySearchParam searchParam) {
        return R.ok();
    }

}
