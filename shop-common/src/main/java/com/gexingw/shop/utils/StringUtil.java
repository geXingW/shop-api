package com.gexingw.shop.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static String trim(CharSequence string, CharSequence suffix) {
        return StrUtil.removePrefix(StrUtil.removeSuffix(string, suffix), suffix);
    }
}
