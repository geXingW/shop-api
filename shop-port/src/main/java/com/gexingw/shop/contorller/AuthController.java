package com.gexingw.shop.contorller;

import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.dto.auth.AuthLoginRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.service.AuthService;
import com.gexingw.shop.service.MemberService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.vo.AuthInfo;
import org.springframework.beans.BeanUtils;
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
            return R.ok(RespCode.AUTHORIZED_FAILED.getCode(), "用户名或密码错误！");
        }

        // 校验密码
        if (!authService.isPasswdMatch(requestParam.getPassword(), umsMember.getPassword())) {
            return R.ok(RespCode.AUTHORIZED_FAILED.getCode(), "用户名或密码错误！");
        }

        // 生成JWT Token
        String token = jwtTokenUtil.generateToken(umsMember.getId(), umsMember.getUsername());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("info", umsMember);

        return R.ok(result, "登录成功！");
    }

    @GetMapping("info")
    public R info() {
        UmsMember member = memberService.getMemberDetailByMemberId(AuthUtil.getAuthId());
        if (member == null) {
            return R.ok("请先登录！");
        }

        AuthInfo authInfo = new AuthInfo();
        BeanUtils.copyProperties(member, authInfo);

        return R.ok(authInfo);
    }
}
