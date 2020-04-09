package com.wq.mmall.advice;

import com.wq.mmall.exception.MmallException;
import com.wq.mmall.vo.response.ServerResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>全局异常处理</h1>
 * Created by Qinyi.
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * <h2>对 CouponException 进行统一处理</h2>
     */
    @ExceptionHandler(value = MmallException.class)
    public ServerResponse<String> handlerCouponException(HttpServletRequest req, MmallException ex) {
        return ServerResponse.createByErrorMessage(ex.getMessage());
    }
}
