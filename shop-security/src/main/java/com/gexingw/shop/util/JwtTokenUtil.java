package com.gexingw.shop.util;

import com.gexingw.shop.bean.ums.UmsAdmin;
import com.gexingw.shop.constant.AuthConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * Created by macro on 2018/4/26.
 */

@Data
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";

    private static final String CLAIM_KEY_ID = "id";

    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.period}")
    private Long expiration;
    @Value("${jwt.token.header}")
    private String tokenHeader;
    @Value("${jwt.token.head}")
    private String tokenHead;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    public String getAuthToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);
        if (authHeader == null || !authHeader.startsWith(tokenHead)) {
            return null;
        }

        return authHeader.substring(tokenHead.length());
    }

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        // 设置Redis数据
        Date createdAt = (Date) claims.get(CLAIM_KEY_CREATED);
        HashMap<String, Object> redisTokenData = new HashMap<String, Object>();
        redisTokenData.put("username", claims.get(CLAIM_KEY_USERNAME));
        redisTokenData.put("id", claims.get(CLAIM_KEY_ID));
        redisTokenData.put("created_at", createdAt.toString());

        // token写入Redis
        String redisKey = AuthConstant.ADMIN_JWT_TOKEN_PREFIX + ":" + DigestUtils.md5DigestAsHex(token.getBytes());
        redisUtil.hmset(redisKey, redisTokenData, expiration * 1000);

        return token;
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取登录用户Id
     *
     * @param token
     * @return
     */
    public Long getAdminIdFromToken(String token) {
        int id;
        try {
            Claims claims = getClaimsFromToken(token);
            id = (int) claims.get("id");
        } catch (Exception e) {
            return null;
        }

        return (long) id;
    }

    /**
     * 验证token是否还有效
     *
     * @param token 客户端传入的token
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        if (expiredDate == null) {
            return true;
        }

        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }

        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UmsAdmin userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_ID, userDetails.getId());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}

