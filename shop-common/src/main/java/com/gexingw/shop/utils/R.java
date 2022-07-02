package com.gexingw.shop.utils;

import com.gexingw.shop.enums.RespCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GeXingW
 */
@Getter
@Setter
public class R<T> {
    /**
     * 凡是该项为 `true`的响应，皆表示成功
     */
    private Boolean success;

    private int code;
    private String message;
    private T data;

    private R() {
    }

    private R(Boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 默认的成功的响应
     *
     * @return R
     */
    public static R ok() {
        return new R(true, RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功的响应
     *
     * @param respCode RespCode
     * @return R
     */
    public static R ok(RespCode respCode) {
        return new R(true, respCode.getCode(), respCode.getMessage(), null);
    }

    public static R ok(Object data) {
        return new R(true, RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    /**
     * 带数据的成功响应
     *
     * @param respCode int
     * @param data     Object
     * @return R
     */
    public static R ok(RespCode respCode, Object data) {
        return new R(true, respCode.getCode(), respCode.getMessage(), data);
    }

    /**
     * 带数据的成功响应
     *
     * @param respCode int
     * @param message  String
     * @return R
     */
    public static R ok(RespCode respCode, String message) {
        return new R(true, respCode.getCode(), message, null);
    }

    /**
     * 带数据的自定义消息的成功响应
     *
     * @param message String
     * @param data    Object
     * @return R
     */
    public static R ok(String message, Object data) {
        return new R(true, RespCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 带数据的自定义消息的成功响应
     *
     * @param data    Object
     * @param message String
     * @return R
     */
    public static R ok(Object data, String message) {
        return new R(true, RespCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 带数据的自定义消息的成功响应
     *
     * @param respCode int
     * @param data     Object
     * @return R
     */
    public static R ok(RespCode respCode, String message, Object data) {
        return new R(true, respCode.getCode(), message, data);
    }

    /**
     * 失败的响应
     *
     * @param respCode RespCode
     * @return R
     */
    public static R failure(RespCode respCode) {
        return new R(false, respCode.getCode(), respCode.getMessage(), null);
    }

    /**
     * 失败的响应
     *
     * @param respCode RespCode
     * @param message  String
     * @return R
     */
    public static R failure(RespCode respCode, String message) {
        return new R(false, respCode.getCode(), message, null);
    }

    /**
     * 带数据的失败响应
     *
     * @param respCode RespCode
     * @param data     Object
     * @return R
     */
    public static R failure(RespCode respCode, Object data) {
        return new R(false, respCode.getCode(), respCode.getMessage(), data);
    }

    /**
     * 带自定义消息和数据的失败响应
     *
     * @param respCode RespCode
     * @param message  String
     * @return R
     */
    public static R failure(RespCode respCode, String message, Object data) {
        return new R(false, respCode.getCode(), message, data);
    }

}
