package com.gexingw.shop.contorller;

import com.gexingw.shop.service.IndexService;
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
}
