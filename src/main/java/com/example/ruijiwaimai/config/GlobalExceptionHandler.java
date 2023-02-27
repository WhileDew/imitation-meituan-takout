package com.example.ruijiwaimai.config;

import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 全局异常处理
 */
@RestControllerAdvice // 绑定Controller
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @param e 异常
     * @return 返回前端
     */
    @ExceptionHandler
    public Result exceptionHandler(Exception e){
        log.error(e.getMessage());
        return Result.error("服务器异常");
    }
}
