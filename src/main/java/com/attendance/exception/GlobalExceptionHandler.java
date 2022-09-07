package com.attendance.exception;

import com.attendance.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    //打印log
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public Result handleHttpMessageNotReadableException(MissingServletRequestParameterException ex){
        logger.error("缺少请求参数,{}",ex.getMessage());
        return Result.error("400","缺少必要的参数");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleUnexpectedServer(Exception ex){
        logger.error("系统异常123：",ex);
        return  Result.error("系统异常");
    }


}
