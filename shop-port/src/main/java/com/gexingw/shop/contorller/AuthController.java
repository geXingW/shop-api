package com.gexingw.shop.contorller;

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
//        address: null
//        balance: null
//        description: null
//        email: null
//        file: null
//        id: null
//        message: "用户登录已过期"
//        phone: null
//        points: null
//        sex: null
//        state: 0
//        token: null
//        username: null

        HashMap<String, Object> map = new HashMap<>();
        map.put("login name:", "Wsf");
        map.put("id", null);
        map.put("state", 0);
        map.put("token", null);

        return R.ok(map);
    }
}
