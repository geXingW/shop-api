package com.gexingw.shop.enums;

import lombok.Getter;

@Getter
public enum RespCode {
    SUCCESS(200000, "success"),

    // 商品异常
    PRODUCT_STOCK_OVER(400001, "商品库存不足！"),

    // 用户认证
    UNAUTHORIZED(401001, "请先登录！"),
    AUTHORIZED_FAILED(401002, "用户名或密码错误！"),
    LOGIN_FAILED(401003, "登出失败！"),
    LOGIN_CAPTCHA_ERROR(401004, "验证码错误！"),
    LOGOUT_ERROR(401005, "注销失败！"),
    ACCESS_DENY(403001, "没有访问权限！"),

    // 请求异常
    REQUEST_METHOD_NOT_SUPPORT(405001, "请求方法错误！"),

    // 参数异常
    PARAMS_INVALID(420001, "参数异常！"),
    ILLEGAL_OPERATION(420002, "非法操作！"),


    // 资源不存在
    RESOURCE_NOT_EXIST(404001, "请求资源不存在！"),
    RESOURCE_UNAVAILABLE(404002, "请求资源不可用！"),

    // 系统异常
    FAILURE(500000, "系统异常，请稍后重试！"),
    QUERY_FAILURE(501000, "查询失败！"),
    SAVE_FAILURE(502000, "保存失败！"),
    UPDATE_FAILURE(503000, "更新失败！"),
    DELETE_FAILURE(504000, "删除失败！"),
    DB_OPERATION_FAILURE(505000, "数据库操作失败！"),
    UPLOAD_FAILURE(506000, "上传失败！"),

    ;


    private int code;
    private String message;

    RespCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
