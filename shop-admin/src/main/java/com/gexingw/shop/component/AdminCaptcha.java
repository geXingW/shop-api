package com.gexingw.shop.component;

import com.gexingw.shop.constant.AuthConstant;
import com.gexingw.shop.util.RedisUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class AdminCaptcha {
    // 验证码类型
    private String charType;

    // 验证码字体样式
    private String fontType;

    // 验证码有效期
    private int period;

    @Autowired
    RedisUtil redisUtil;

    public SpecCaptcha generate() throws IOException, FontFormatException {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);

        specCaptcha.setCharType(getCharType());
        specCaptcha.setFont(getFontType());

        return specCaptcha;
    }

    private int getCharType() {
        if ("TYPE_DEFAULT".equals(charType)) {
            return Captcha.TYPE_DEFAULT;
        }

        if ("TYPE_ONLY_NUMBER".equals(charType)) {
            return Captcha.TYPE_ONLY_NUMBER;
        }

        if ("TYPE_ONLY_CHAR".equals(charType)) {
            return Captcha.TYPE_ONLY_CHAR;
        }

        if ("TYPE_ONLY_UPPER".equals(charType)) {
            return Captcha.TYPE_ONLY_UPPER;
        }

        if ("TYPE_ONLY_LOWER".equals(charType)) {
            return Captcha.TYPE_ONLY_LOWER;
        }

        if ("TYPE_NUM_AND_UPPER".equals(charType)) {
            return Captcha.TYPE_NUM_AND_UPPER;
        }

        throw new RuntimeException("验证码类型配置错误!");
    }

    private int getFontType() {
        if ("FONT_1".equals(fontType)) {
            return Captcha.FONT_1;
        }

        if ("FONT_2".equals(fontType)) {
            return Captcha.FONT_2;
        }

        if ("FONT_3".equals(fontType)) {
            return Captcha.FONT_3;
        }

        if ("FONT_4".equals(fontType)) {
            return Captcha.FONT_4;
        }

        if ("FONT_5".equals(fontType)) {
            return Captcha.FONT_5;
        }

        if ("FONT_6".equals(fontType)) {
            return Captcha.FONT_6;
        }

        if ("FONT_7".equals(fontType)) {
            return Captcha.FONT_7;
        }

        if ("FONT_8".equals(fontType)) {
            return Captcha.FONT_8;
        }

        if ("FONT_9".equals(fontType)) {
            return Captcha.FONT_9;
        }

        if ("FONT_10".equals(fontType)) {
            return Captcha.FONT_10;
        }

        throw new RuntimeException("验证码字体配置错误!");
    }

    public boolean cacheCode(String uuid, String code) {
        // 生成uuid
        String redisKey = getRedisKey() + ":" + uuid;

        // 写入redis
        redisUtil.set(redisKey, code.toLowerCase(), period);

        // 检查Redis中Key是否存在
        return redisUtil.hasKey(redisKey);
    }

    public boolean checkCode(String uuid, String code) {
        // 根据uuid从redis获取验证码
        String cacheCode = (String) redisUtil.get(getRedisKey() + ":" + uuid);
        if (cacheCode == null) {
            return false;
        }

        return code.toLowerCase().equals(cacheCode);
    }

    private String getRedisKey() {
        return AuthConstant.ADMIN_LOGIN_CAPTCHA;
    }
}
