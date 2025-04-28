package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice // 使用@RestControllerAdvice注解，表明这是一个全局的异常处理器，用于统一处理整个应用程序中的异常。
@Slf4j
public class GlobalExceptionHandler {

    /*
    * 当使用@ExceptionHandler注解时，如果后面没有明确指定要处理的异常类（即不写@ExceptionHandler(BaseException.class)），
    * Spring会根据方法参数中的异常类型来判断该方法可以处理哪些异常。它会遍历所有带有@ExceptionHandler注解的方法，检查方法参数中的
    * 异常类型，看是否与当前抛出的异常类型匹配。
    * */
    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // 这里指明了捕获哪种异常
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            // username已存在
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }
        else{
            // 未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
