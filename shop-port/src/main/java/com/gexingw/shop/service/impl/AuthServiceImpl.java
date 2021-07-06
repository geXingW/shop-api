package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bean.OAuthAccount;
import com.gexingw.shop.bo.UserDetail;
import com.gexingw.shop.mapper.OAuthAccountMapper;
import com.gexingw.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private OAuthAccountMapper authAccountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println(username);

        // 根据用户名查找数据库
        OAuthAccount account = authAccountMapper.selectOne(new QueryWrapper<OAuthAccount>().eq("username", username));
        return new UserDetail(account);
    }
}
