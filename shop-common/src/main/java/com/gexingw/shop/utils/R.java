package com.gexingw.shop.utils;

import com.gexingw.shop.enums.RespCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R<T> {
    private int status;
    private String message;
    private T data;

    public R() {
    }

    public R(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static R ok() {
        return new R(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), null);
    }

    public static R ok(int status) {
        return new R(status, RespCode.SUCCESS.getMessage(), null);
    }

    public static R ok(int status, String message) {
        return new R(status, message, null);
    }

    public static R ok(int status, String message, Object data) {
        return new R(status, message, data);
    }

    public static R ok(Object data) {
        return new R(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public static R ok(String message) {
        return new R(RespCode.SUCCESS.getCode(), message, null);
    }

    public static R ok(String message, String data) {
        return new R(RespCode.SUCCESS.getCode(), message, data);
    }

    public static R ok(String message, Object data) {
        return new R(RespCode.SUCCESS.getCode(), message, data);
    }

    public static R ok(Object data, String message) {
        return new R(RespCode.SUCCESS.getCode(), message, data);
    }

}
