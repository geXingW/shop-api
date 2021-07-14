package com.gexingw.shop.controller;

import com.gexingw.shop.dto.banner.BannerRequestParam;
import com.gexingw.shop.dto.banner.BannerSearchParam;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("banner")
public class BannerController {

    @GetMapping
    public R index(BannerSearchParam searchParam) {
        return R.ok();
    }

    @PostMapping
    public R save(BannerRequestParam requestParam) {
        return R.ok("已保存！");
    }

    @PutMapping
    public R update(BannerRequestParam requestParam) {
        return R.ok("已更新！");
    }

    @DeleteMapping
    public R delete(Set<Long> ids) {
        return R.ok("已删除！");
    }
}
