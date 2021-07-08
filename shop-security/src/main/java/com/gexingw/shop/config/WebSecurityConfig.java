package com.gexingw.shop.config;

import com.alibaba.fastjson.JSON;
import com.gexingw.shop.service.AuthService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthService authService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/check_token", "/oauth/authorize", "/favicon.ico", "/resources/**", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll();

        http.authorizeRequests().anyRequest().authenticated();
////        http.formLogin().loginPage("")
//        // 首页无需登陆
////        http.formLogin().loginPage("/login").failureUrl("/login?error").permitAll();
////        http.requestMatchers().antMatchers("/login", "/login-error", "/oauth/authorize", "/oauth/token")
////                .and()
////                .authorizeRequests()
////                .antMatchers("/", "/login", "/oauth/authorize", "/oauth/token", "/oauth/check_token", "/favicon.ico", "/resources/**", "/error").permitAll();
//        http.authorizeRequests().antMatchers("/", "/favicon.ico", "/resources/**", "/error").permitAll();
//
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            R resp = R.ok(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            httpServletResponse.getWriter().write(JSON.toJSONString(resp));
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (String username) -> authService.loadUserByUsername(username);
    }

}
