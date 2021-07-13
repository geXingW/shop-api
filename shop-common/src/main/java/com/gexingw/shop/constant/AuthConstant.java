package com.gexingw.shop.constant;

public interface AuthConstant {

    // Jwt token
    public static final String JWT_TOKEN_PREFIX = "auth:jwt-token";

    public static final String ADMIN_JWT_TOKEN_PREFIX = "admin:jwt-token";

    public static final String MEMBER_JWT_TOKEN_PREFIX = "member:jwt-token";

    // 登录验证码存储
    public static final String ADMIN_LOGIN_CAPTCHA = "admin:login:captcha";

    public static final String CLIENT_ADMIN = "admin";

    public static final String CLIENT_MEMBER = "member";
}
