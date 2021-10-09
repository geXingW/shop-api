package com.gexingw.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Data
//@Configuration
//@ConfigurationProperties(prefix = "snowflake")
public class SnowFlakeConfig {

    /**
     * 起始时间
     */
    private long startTimestamp = 1633017600000L;

    /**
     * 数据中心所占的位数
     */
    private long dataCenterBits = 5L;

    /**
     * 机器码所占的位数
     */
    private long machineBits = 5L;

    /**
     * 序列号所占的位数
     */
    private long sequenceBits = 12L;
}
