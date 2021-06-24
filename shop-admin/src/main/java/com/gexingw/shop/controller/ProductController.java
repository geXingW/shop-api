package com.gexingw.shop.controller;

import com.gexingw.shop.dto.product.PmsProductSearchParam;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping
    public R index(@RequestParam PmsProductSearchParam searchParam) {
        return R.ok();
    }
}
