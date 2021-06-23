package com.gexingw.shop.exception;

import com.gexingw.shop.enums.RespCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private Integer errCode;
    private String errMsg;

    public ApiException() {
        super();
    }

    public ApiException(String message, Integer errCode) {
        super(message);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public ApiException(String message, Throwable cause, Integer errCode) {
        super(message, cause);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public ApiException(Throwable cause, Integer errCode) {
        super(cause);
        this.errCode = errCode;
        this.errMsg = RespCode.FAILURE.getMessage();
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer errCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
        this.errMsg = message;
    }
}
