package com.gexingw.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/auth")
                .and()
                .authorizeRequests()
                .antMatchers("/auth").access("#oauth2.hasScope('read')")
                .antMatchers("/oauth/**", "/login").permitAll()
                .anyRequest().authenticated();
    }
}
