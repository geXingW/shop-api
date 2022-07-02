package com.gexingw.shop.modules.sys.controller;

import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.sys.dto.AuthLoginRequestParam;
import com.gexingw.shop.modules.sys.service.AuthService;
import com.gexingw.shop.modules.sys.vo.AuthInfo;
import com.gexingw.shop.modules.ums.service.MemberService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    MemberService memberService;

    @GetMapping
    public R index() {
        return R.ok();
    }

    @PostMapping("login")
    public R login(@RequestBody AuthLoginRequestParam requestParam) {
        // 根据用户名从数据库查询用户信息
        UmsMember umsMember = authService.getMemberDetailByUsername(requestParam.getUsername());
        if (umsMember == null) {
            return R.ok(RespCode.AUTHORIZED_FAILED);
        }

        // 校验密码
        if (!authService.isPasswdMatch(requestParam.getPassword(), umsMember.getPassword())) {
            return R.ok(RespCode.AUTHORIZED_FAILED);
        }

        // 生成JWT Token
        String token = jwtTokenUtil.generateToken(umsMember.getId(), umsMember.getUsername());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("info", umsMember);

        return R.ok(result);
    }

    @GetMapping("info")
    public R info() {
        UmsMember member = memberService.getMemberDetailByMemberId(AuthUtil.getAuthId());
        if (member == null) {
            return R.failure(RespCode.UNAUTHORIZED);
        }

        AuthInfo authInfo = new AuthInfo();
        BeanUtils.copyProperties(member, authInfo);

        return R.ok(authInfo);
    }

    @DeleteMapping("logout")
    public R logout() {
        return authService.logout() ? R.ok() : R.ok(RespCode.LOGOUT_ERROR);
    }
}
