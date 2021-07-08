package com.gexingw.shop.contorller;

import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping
    public R index() {
        return R.ok("Welcome, 无需登录！");
    }

    @GetMapping("home")
    public R home(){
        return R.ok("Home Page，无需登录！");
    }
}
