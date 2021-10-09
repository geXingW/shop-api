package com.gexingw.shop.utils;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConfigurationProperties(prefix = "order-id-generator")
public class IdGenerator implements IdentifierGenerator {

    private final static long START_TIMESTAMP = 1412776042000L;
    private final static long DEFAULT_CLOCK_BACKWARD_ALLOW_OFFSET_MS = 5L;

    private final static long MACHINE_ID_BITS = 8L;
    private final static long SEQUENCE_BITS = 8L;

    private final static long MAX_MACHINE_ID = -1L ^ (-1L << MACHINE_ID_BITS);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);

    private final static long TIMESTAMP_OFFSET = MACHINE_ID_BITS + SEQUENCE_BITS;
    private final static long MACHINE_ID_OFFSET = SEQUENCE_BITS;

    private long machineId;
    private long lastTimestamp = 0L;
    private long lastSequence = 0L;
    private long clockBackwardAllowOffsetMS;

    public IdGenerator() {
        this(getMachineId(), DEFAULT_CLOCK_BACKWARD_ALLOW_OFFSET_MS);
    }

    public IdGenerator(long machineId) {
        this(machineId, DEFAULT_CLOCK_BACKWARD_ALLOW_OFFSET_MS);
    }

    public IdGenerator(long machineId, long clockBackwardAllowOffsetMS) {
        checkMachineId(machineId);

        this.machineId = machineId;
        this.clockBackwardAllowOffsetMS = clockBackwardAllowOffsetMS;
    }

    public synchronized Long nextId(Object entity) {
        long currentTimestamp = this.getCurrentTimestamp();

        // 当前时间与上一时间的时间差
        long timestampOffset = currentTimestamp - this.lastTimestamp;

        // 检查时钟回拨
        if (timestampOffset < 0) {
            if (timestampOffset > this.clockBackwardAllowOffsetMS) { // 时钟回拨时间超过允许时间，抛出异常
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", TIMESTAMP_OFFSET));
            }

            // 等待时钟赶上，重新获取时间戳
            try {
                wait(timestampOffset << 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            currentTimestamp = this.getCurrentTimestamp();
        }

        // 序列号递增
        this.lastSequence = (this.lastSequence + 1) & MAX_SEQUENCE;
        if (this.lastSequence == 0L) {
            currentTimestamp = this.getNextTimestamp(currentTimestamp);
            this.lastSequence = ThreadLocalRandom.current().nextLong(0, 3);
        }

        this.lastTimestamp = currentTimestamp;

        return (currentTimestamp - START_TIMESTAMP) << TIMESTAMP_OFFSET |
                this.machineId << MACHINE_ID_OFFSET |
                this.lastSequence;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public void setClockBackwardAllowOffsetMS(long offsetMS) {
        this.clockBackwardAllowOffsetMS = offsetMS;
    }

    private static boolean checkMachineId(long machineId) {
        if (MAX_MACHINE_ID < machineId) {
            throw new IllegalArgumentException("Machine Id can't be greater than " + MAX_MACHINE_ID + " or less than 0");
        }

        return true;
    }

    private long getNextTimestamp(long currentTimestamp) {
        long nextTimestamp = this.getCurrentTimestamp();
        while (nextTimestamp <= currentTimestamp) {
            nextTimestamp = this.getCurrentTimestamp();
        }

        return nextTimestamp;
    }

    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    protected static long getMachineId() {
        long lastIp = 0L;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] addressByte = inetAddress.getAddress();
            lastIp = addressByte[addressByte.length - 1];

        } catch (Exception e) {
            throw new RuntimeException("Unknown Host Exception", e);
        }

        return 0x000000FF & lastIp;
    }
}
