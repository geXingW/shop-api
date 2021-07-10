package com.gexingw.shop.controller;

import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @PostMapping("login")
    public R login() {
        return R.ok("Auth log page.....");
    }

    @GetMapping("info")
    public R info(){
        return R.ok("Auth info page .....");
    }
}
