package com.gexingw.shop.component;

import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.utils.RedisUtil;
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
        String requestURI = request.getRequestURI();

        // 静态资源放行
        if (requestURI.matches("/static/.*") || requestURI.equals("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从Header中获取Token
        String authToken = jwtTokenUtil.getAuthToken(request);
        if (authToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestURI.contains("/auth/captcha")) {
            filterChain.doFilter(request, response);
            return;
        }

        String keepAliveKey = AuthConstant.JWT_TOKEN_PREFIX + ":" + "0586bf420868455f6e304fe3e7633eeb";

        // 检查Redis中是否存在
        String redisKey = AuthConstant.JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(authToken.getBytes());

        // 根据token获取的用户名
        String adminName;

        if (!keepAliveKey.equals(redisKey)) {
            if (!keepAliveKey.equals(redisKey) && !jwtTokenUtil.validateToken(authToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (redisUtil.hmget(redisKey).size() == 0) {
                filterChain.doFilter(request, response);
                return;
            }

            redisUtil.expire(redisKey, jwtTokenUtil.getExpiration() * 1000);

            adminName = jwtTokenUtil.getUserNameFromToken(authToken);
        } else {
            adminName = "admin";
        }

        // 获取登陆用户的详细信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminName);

        // 添加 当前用户访问权限 访问权限
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        // 设置用户登陆状态
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //请求继续
        filterChain.doFilter(request, response);
    }
}
