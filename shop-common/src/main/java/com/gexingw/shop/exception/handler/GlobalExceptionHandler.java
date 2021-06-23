package com.gexingw.shop.exception.handler;

import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.exception.*;
import com.gexingw.shop.utils.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public R exceptionHandler(HttpServletRequest req, ApiException e) {
        return R.ok(e.getErrCode(), e.getErrMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return R.ok(RespCode.PARAMS_INVALID.getCode(), message);
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<R> exceptionHandler(HttpServletRequest request, BadRequestException e) {
        return buildResponseEntity(R.ok(e.getCode(), e.getMessage()), e.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public R exceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return R.ok(RespCode.REQUEST_METHOD_NOT_SUPPORT.getCode(), e.getMessage());
    }

    /**
     * 资源不存在异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ResourceNotExistException.class)
    public R exceptionHandler(HttpServletRequest req, ResourceNotExistException e) {
        return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), e.getMessage());
    }

    /**
     * 数据操作失败异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DBOperationException.class)
    public R exceptionHandler(HttpServletRequest req, DBOperationException e) {
        return R.ok(RespCode.DB_OPERATION_FAILURE.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalOperationException.class)
    public R exceptionHandler(HttpServletRequest req, IllegalOperationException e) {
        return R.ok(RespCode.DB_OPERATION_FAILURE.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        return R.ok(RespCode.FAILURE.getCode(), e.getMessage());
    }

    /**
     * 统一返回
     */
    private ResponseEntity<R> buildResponseEntity(R r, int httpStatus) {
        return new ResponseEntity<>(r, HttpStatus.valueOf(httpStatus));
    }
}
