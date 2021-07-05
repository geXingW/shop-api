package com.gexingw.shop.contorller;

import com.gexingw.shop.dto.auth.AuthLoginRequestParam;
import com.gexingw.shop.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("auth")
public class AuthController {
    @GetMapping
    public R index() {
        return R.ok();
    }

    @PostMapping("login")
    public R login() {
        return R.ok("登录成功！");
    }

    @GetMapping("info")
    public R info() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("login name:", "Wsf");

        return R.ok(map);
    }
}
