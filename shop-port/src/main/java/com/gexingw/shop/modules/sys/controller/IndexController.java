package com.gexingw.shop.modules.sys.controller;

import com.gexingw.shop.modules.sys.service.IndexService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    IndexService indexService;

    @GetMapping
    public R index() {
        return R.ok("Welcome, 无需登录！");
    }

    @GetMapping("home")
    public R home() {
        return R.ok("Home Page，无需登录！");
    }

    /**
     * 首页轮播图
     *
     * @return
     */
    @GetMapping("banner")
    public R banner() {
        return R.ok(indexService.getBannerList());
    }

    @GetMapping("hot-product")
    public R hotProduct(){
        return R.ok(indexService.getHotProduct());
    }

    @GetMapping("recommend-product")
    public R recommendProduct(){
        return R.ok(indexService.getRecommendProduct());
    }

    @GetMapping("new-product")
    public R newProduct(){
        return R.ok(indexService.getNewProduct());
    }
}
