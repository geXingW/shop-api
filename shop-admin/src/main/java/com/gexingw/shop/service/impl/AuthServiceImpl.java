package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bean.AuthClientDetail;
import com.gexingw.shop.bean.OAuthAccount;
import com.gexingw.shop.bo.UserDetail;
import com.gexingw.shop.bo.ums.UmsMenu;
import com.gexingw.shop.exception.AuthenticationErrorException;
import com.gexingw.shop.mapper.AuthMapper;
import com.gexingw.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println(username);

        // 根据用户名查找数据库
        OAuthAccount account = authMapper.selectOne(new QueryWrapper<OAuthAccount>().eq("username", username));

        // 查找用户绑定的Client详情
        AuthClientDetail clientDetail = authMapper.getClientDetailsByClientId(account.getClientId());
        if (clientDetail == null) {
            throw new AuthenticationErrorException("Client信息错误！");
        }

//        if (AuthConstant.CLIENT_ADMIN.equals(clientDetail.getScope())) { // 如果是管理员用户，查询权限
//
//        }
//
//        if (AuthConstant.CLIENT_MEMBER.equals(clientDetail.getScope())) { // 如果是管理员用户
//
//        }

//        getAdminPermissionsByAdminId(account.getId());

        return new UserDetail(account);
    }

    @Override
    public List<String> getAdminPermissionsByAdminId(Long adminId) {
        List<String> permissions = new ArrayList<>();
        // 尝试从redis获取 TODO

        // 从数据库获取
        List<UmsMenu> adminPermissions = authMapper.getAdminPermissions(adminId);
        if (adminPermissions != null) {
            permissions = adminPermissions.stream()
                    .map(UmsMenu::getPermission)
                    .filter(Objects::nonNull)
                    .filter(string -> !string.isEmpty())
                    .collect(Collectors.toList());
        }

        // 重新写入redis TODD

        return permissions;
    }
}
