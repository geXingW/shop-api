package com.gexingw.shop.exception.handler;

import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.exception.*;
import com.gexingw.shop.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ResponseBody
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public R exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        String message = null;
//        if (bindingResult.hasErrors()) {
//            FieldError fieldError = bindingResult.getFieldError();
//            if (fieldError != null) {
//                message = fieldError.getField() + fieldError.getDefaultMessage();
//            }
//        }
//
//        return R.failure(RespCode.PARAMS_INVALID.getCode(), message);
//    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public R exceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return R.failure(RespCode.PARAMS_INVALID, objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BadRequestException.class)
    public R exceptionHandler(HttpServletRequest request, BadRequestException e) {
        return R.failure(RespCode.BAD_REQUEST, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public R exceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return R.failure(RespCode.REQUEST_METHOD_NOT_SUPPORT, e.getMessage());
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
        return R.failure(RespCode.RESOURCE_NOT_EXIST, e.getMessage());
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
        return R.failure(RespCode.DB_OPERATION_FAILURE, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalOperationException.class)
    public R exceptionHandler(HttpServletRequest req, IllegalOperationException e) {
        return R.failure(RespCode.DB_OPERATION_FAILURE, e.getMessage());
    }

    /**
     * 认证失败
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AuthenticationErrorException.class)
    public R exceptionHandler(HttpServletRequest req, AuthenticationErrorException e) {
        return R.failure(RespCode.UNAUTHORIZED, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        return R.failure(RespCode.FAILURE, e.getMessage());
    }

}
