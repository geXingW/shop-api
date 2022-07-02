package com.gexingw.shop.modules.pms.controller;

import com.gexingw.shop.modules.pms.service.PmsProductCategoryService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GeXingW
 */
@RestController
@RequestMapping("product/category")
public class PmsProductCategoryController {

    @Autowired
    PmsProductCategoryService categoryService;

    @GetMapping("tree")
    public R tree() {
        return R.ok(categoryService.getCategoryTree());
    }

}
