package com.gexingw.shop.contorller;

import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.dto.auth.AuthLoginRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.AuthService;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public R index() {
        return R.ok();
    }

    @PostMapping("login")
    public R login(@RequestBody AuthLoginRequestParam requestParam) {
        // 根据用户名从数据库查询用户信息
        UmsMember umsMember = authService.getMemberDetailByUsername(requestParam.getUsername());
        if (umsMember == null) {
            return R.ok(RespCode.AUTHORIZED_FAILED.getCode(), "用户名或密码错误！");
        }

        // 校验密码
        if(!authService.isPasswdMatch(requestParam.getPassword(), umsMember.getPassword())){
            return R.ok(RespCode.AUTHORIZED_FAILED.getCode(), "用户名或密码错误！");
        }

        // 生成JWT Token
        String token = jwtTokenUtil.generateToken(umsMember.getId(), umsMember.getUsername());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("id", umsMember.getId());

        return R.ok(result, "登录成功！");
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
