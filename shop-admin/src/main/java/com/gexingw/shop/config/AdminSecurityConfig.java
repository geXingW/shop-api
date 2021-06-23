package com.gexingw.shop.config;

import com.gexingw.shop.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfig extends SecurityConfig{

    @Autowired
    UmsAdminService umsAdminService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> umsAdminService.loadUserByUsername(username);
    }
}
