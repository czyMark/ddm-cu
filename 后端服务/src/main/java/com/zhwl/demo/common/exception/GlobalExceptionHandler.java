package com.zhwl.demo.common.exception;


import com.zhwl.demo.base.AjaxResult;
import com.zhwl.demo.exception.NoLoginException;
import com.zhwl.demo.exception.TokenNullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yxs
 * @desc 全局捕获异常
 * @date 2021-04-26 11:24
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult illegalArgumentExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("###全局捕获异常###，error：{}",e);
        return AjaxResult.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("###全局捕获异常###，error：{}",e);
        return AjaxResult.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoLoginException.class)
    public AjaxResult noLoginException(NoLoginException e) {
        return AjaxResult.noLoginError(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(TokenNullException.class)
    public AjaxResult tokenNullException(TokenNullException e) {
        return AjaxResult.noLoginError(e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return AjaxResult.error("参数丢失");
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult runtimeException(RuntimeException e) {
        log.error("###全局捕获异常###，error：{}",e);
        return AjaxResult.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionHandler(Exception e) {
        log.error("###全局捕获异常###，error：{}",e);
        return AjaxResult.error("系统错误，请稍后重试");
    }
}
