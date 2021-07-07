package com.gexingw.shop.config;

import com.gexingw.shop.component.JwtAuthenticationTokenFilter;
import com.gexingw.shop.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用csrf
        http.csrf().disable();

        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 放行所有请求
        http.authorizeRequests().antMatchers("/auth/login", "/auth/captcha", "/test/*").permitAll();

        // 出以上接口之外都需要认证所有请求都需要认证
        http.authorizeRequests().anyRequest().authenticated();

        // 添加登陆认证拦截器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 设置拒绝访问的处理
        http.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
