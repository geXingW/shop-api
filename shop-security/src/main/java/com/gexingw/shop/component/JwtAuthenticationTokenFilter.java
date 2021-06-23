package com.gexingw.shop.component;

import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从Header中获取Token
        String authToken = jwtTokenUtil.getAuthToken(request);
        if (authToken == null || request.getRequestURI().contains("/auth/captcha")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查Redis中是否存在
        String redisKey = AuthConstant.ADMIN_JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(authToken.getBytes());
        if (redisUtil.hmget(redisKey).size() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        // 重置token有效期
        redisUtil.expire(redisKey, jwtTokenUtil.getExpiration() * 1000);

        // 获取登陆用户的详细信息
        String adminName = jwtTokenUtil.getUserNameFromToken(authToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminName);

        if (!jwtTokenUtil.validateToken(authToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 添加 当前用户访问权限 访问权限
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        // 设置用户登陆状态
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //请求继续
        filterChain.doFilter(request, response);
    }
}
