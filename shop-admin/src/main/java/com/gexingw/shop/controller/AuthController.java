package com.gexingw.shop.controller;

import com.gexingw.shop.bo.ums.UmsAdmin;
import com.gexingw.shop.component.AdminCaptcha;
import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.dto.auth.UmsLoginRequestParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.exception.BadRequestException;
import com.gexingw.shop.service.UmsAdminService;
import com.gexingw.shop.service.UmsDeptService;
import com.gexingw.shop.service.UmsJobService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.util.RedisUtil;
import com.gexingw.shop.utils.R;
import com.gexingw.shop.utils.RsaUtil;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AdminCaptcha adminCaptcha;

    @Autowired
    private RsaUtil rsaUtil;

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UmsJobService umsJobService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsDeptService deptService;

    /**
     * 登陆接口
     *
     * @return
     */
    @PostMapping("login")
    public R login(@Validated @RequestBody UmsLoginRequestParam loginParams) {
        // 检查校验码是否正确
        if (!adminCaptcha.checkCode(loginParams.getUuid(), loginParams.getCode())) {
            throw new BadRequestException(RespCode.LOGOUT_CAPTCHA_ERROR.getCode(), RespCode.LOGOUT_CAPTCHA_ERROR.getMessage());
        }

        // 解密password
        String decodePasswd;
        try {
            decodePasswd = rsaUtil.decryptByPrivateKey(loginParams.getPassword());
        } catch (Exception e) {
            throw new BadRequestException(RespCode.AUTHORIZED_FAILED.getCode(), RespCode.AUTHORIZED_FAILED.getMessage());
        }

        UmsAdmin userDetails = umsAdminService.findByUserName(loginParams.getUsername());
        // 生成登陆的token
        if (userDetails == null) {
            throw new BadRequestException(RespCode.AUTHORIZED_FAILED.getCode(), "用户名未找到！");
        }

        if (!passwordEncoder.matches(decodePasswd, userDetails.getPassword())) {
            throw new BadRequestException(RespCode.AUTHORIZED_FAILED.getCode(), "用户名或密码错误！");
        }
        String token = jwtTokenUtil.generateToken(userDetails);

        // 获取用户已分配角色
        Map<String, Object> userInfo = new HashMap<>();

        userDetails.setPassword(null);// 移除密码字段
        userInfo.put("info", userDetails);
        userInfo.put("roles", umsAdminService.getUserPermissions(userDetails.getId()));// 角色
        userInfo.put("dataScopes", umsAdminService.getUserDataScope(userDetails.getId()));// 数据权限
        userInfo.put("jobs", umsJobService.getJobsByAdminId(userDetails.getId()));// jobs

        HashMap<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userInfo);

        return R.ok("登陆成功！", result);
    }

    /**
     * 验证码接口
     *
     * @return
     */
    @GetMapping("captcha")
    public R captcha() {
        try {
            SpecCaptcha specCaptcha = adminCaptcha.generate();
            String code = specCaptcha.text();

            // 生成uuid
            String uuid = UUID.randomUUID().toString();

            // 缓存验证码
            if (!adminCaptcha.cacheCode(uuid, code)) {
                return R.ok(RespCode.FAILURE.getCode(), "验证码生成失败！");
            }

            HashMap<String, String> result = new HashMap<>();
            result.put("key", uuid);
            result.put("image", specCaptcha.toBase64());

            return R.ok(result);
        } catch (Exception e) {
            return R.ok(RespCode.FAILURE.getCode(), "验证码生成失败！");
        }
    }

    @GetMapping("info")
    public R info() {
        UmsAdmin userDetails = umsAdminService.getAdminDetailByAdminName(AuthUtil.getAuthUsername());

        Map<String, Object> userInfo = new HashMap<>();

        // 角色
        userInfo.put("roles", umsAdminService.getUserPermissions(userDetails.getId()));

        // 所属部门
        userDetails.setDept(deptService.getAdminDeptByAdminId(userDetails.getId()));

        // 移除密码字段
        userDetails.setPassword(null);
        userInfo.put("info", userDetails);
        // 数据权限
        userInfo.put("dataScopes", umsAdminService.getUserDataScope(userDetails.getId()));
        // jobs
        userInfo.put("jobs", umsJobService.getJobsByAdminId(userDetails.getId()));

        HashMap<String, Object> result = new HashMap<>();
        result.put("user", userInfo);
        return R.ok(userInfo);
    }

    @DeleteMapping("/logout")
    R logout(HttpServletRequest request) {
        String token = jwtTokenUtil.getAuthToken(request);

        String tokenKey = AuthConstant.ADMIN_JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(token.getBytes());

        redisUtil.del(tokenKey);

        if (redisUtil.hasKey(tokenKey)) {
            return R.ok(RespCode.LOGOUT_FAILED.getCode(), RespCode.LOGOUT_FAILED.getMessage());
        }

        return R.ok("已登出！");
    }
}
