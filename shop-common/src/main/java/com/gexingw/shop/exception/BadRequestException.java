package com.gexingw.shop.exception;

import com.gexingw.shop.enums.RespCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {

    // http状态码
    private Integer status = HttpStatus.BAD_REQUEST.value();

    // 错误码
    private int code = RespCode.SUCCESS.getCode();

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BadRequestException(int code, String msg, HttpStatus status) {
        super(msg);
        this.code = code;
        this.status = status.value();
    }
}

