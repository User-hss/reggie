package com.hss.reggie.exception;

import com.hss.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)//处理的异常类型
    public R<String> doAddException(SQLIntegrityConstraintViolationException e){
        if (e.getMessage().contains("Duplicate entry")) {
            String msg=e.getMessage().split(" ")[2];
            return R.error(msg+"已存在！");
        }
        log.error(e.getMessage());
        return R.error("未知异常，请重试！");
    }
    @ExceptionHandler(value = IOException.class)//文件传输异常
    public R<String> doIOException(IOException e){
        log.error(e.getMessage());
        return R.error("图片资源未找到");
    }
    @ExceptionHandler(value = CustomException.class)//自定义业务异常
    public R<String> doCustomException(CustomException e){
        return R.error(e.getMessage());
    }
//    @ExceptionHandler
    public R<String> doException(Throwable e){
        log.error(e.getMessage());
        return R.error("系统异常，请重试！");
    }
}
