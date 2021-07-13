package com.gexingw.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 添加登陆认证拦截器
////        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        // 设置拒绝访问的处理
////        http.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler);
//    }

    /**
     * 常规登录失败处理器
     */
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return (httpServletRequest, httpServletResponse, e) -> {
//            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            AuthResp resp = new AuthResp(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
//            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(resp));
//        };
//    }

}
