package com.gexingw.shop.component;

import com.gexingw.shop.bo.ums.UmsMember;
import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.service.AuthService;
import com.gexingw.shop.util.JwtTokenUtil;
import com.gexingw.shop.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从Header中获取Token
        String authToken = jwtTokenUtil.getAuthToken(request);
        if (authToken == null || request.getRequestURI().contains("/auth/captcha")) {
             filterChain.doFilter(request, response);
            return;
        }

        // 验证token是有有效
        if (!jwtTokenUtil.validateToken(authToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查Redis中是否存在
        String redisKey = AuthConstant.JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(authToken.getBytes());
        if (redisUtil.hmget(redisKey).size() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        // 重置token有效期
        redisUtil.expire(redisKey, jwtTokenUtil.getExpiration() * 1000);

        // 获取登陆用户的详细信息
        String memberName = jwtTokenUtil.getUserNameFromToken(authToken);
        UserDetails userDetails = authService.loadUserByUsername(memberName);
        if(userDetails == null){
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
