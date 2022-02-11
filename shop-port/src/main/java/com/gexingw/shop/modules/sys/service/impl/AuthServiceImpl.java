package com.gexingw.shop.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.modules.ums.bo.UmsMemberDetail;
import com.gexingw.shop.mapper.ums.UmsMemberMapper;
import com.gexingw.shop.modules.sys.service.AuthService;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UmsMemberMapper memberMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        UmsMember admin = getMemberDetailByUsername(username);
        if (admin == null) {
            return null;
        }

        // 组装security 权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        return new UmsMemberDetail(admin, authorities);
    }

    @Override
    public boolean isPasswdMatch(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }

    public UmsMember getMemberDetailByUsername(String username) {
        return memberMapper.selectOne(new QueryWrapper<UmsMember>().eq("username", username));
    }

    @Override
    public boolean logout() {
        // 从请求中获取token
        String authToken = jwtTokenUtil.getAuthToken(request);

        // 根据用户token，删除用户登录信息
        String redisKey = AuthConstant.JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(authToken.getBytes());
        redisUtil.del(redisKey);

        // 检查用户登录信息是否删除
        return !redisUtil.hasKey(redisKey);
    }
}
