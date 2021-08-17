package com.gexingw.shop;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ShopPortApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("test"));
        System.out.println(passwordEncoder.encode("client"));

    }

    @Test
    void testSnowflake() {
        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (Integer i = 0; i < 10; i++){
            System.out.println(snowflake.nextId());
        }

        //参数1为终端ID
        //参数2为数据中心ID
        snowflake = IdUtil.getSnowflake(1, 2);
        for (Integer i = 0; i < 10; i++){
            System.out.println(snowflake.nextId());
        }
    }
}
