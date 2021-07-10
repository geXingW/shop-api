package com.gexingw.shop.controller;

import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public R index() {
        return R.ok("Index page ....");
    }

    @GetMapping("home")
    public R home() {
        return R.ok("Home page ....");
    }
}
