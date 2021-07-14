package com.gexingw.shop.contorller;

import com.gexingw.shop.dto.product.ProductSearchParam;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping("/")
    R index(ProductSearchParam searchParam) {
        return R.ok();
    }
}
