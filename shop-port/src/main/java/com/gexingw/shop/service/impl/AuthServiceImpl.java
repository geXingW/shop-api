package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.UmsMemberDetail;
import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.mapper.UmsMemberMapper;
import com.gexingw.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UmsMemberMapper memberMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

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
}
