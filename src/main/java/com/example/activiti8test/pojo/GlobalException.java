package com.example.activiti8test.pojo;

import com.example.activiti8test.enmu.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @projectName: Activiti8Test
 * @package: com.example.activiti8test.pojo
 * @className: GlobalException
 * @author: KiMa
 * @description: 统一异常信息处理类：
 * 被@RestControllerAdvice注解可以使用 @ExceptionHandler，@InitBinder和@ModelAttribute这些注解
 * **@ExceptionHandler(Exception.class)**可以捕获到所传入的异常类型，例如NullPointerException、
 * FileNotFoundException等异常（比如下述代码中，第一个可以捕获所有的异常，Exception是所有异常的父类。第二个尽可以捕获BaseException类型的异常）
 * 捕获到异常之后我们选择使用统一结果集类进行返回提示系统出现异常
 * @date: 2023-12-12 16:54
 * @version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * 全局异常处理
     *
     * @param e 异常信息
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> globalException(Exception e) {
        log.error("异常信息 => {}", e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail(HttpCodeEnum.OPERATOR_IS_FAILED.getMessage());
    }

    /**
     * 基础异常处理
     *
     * @param baseException 基础异常信息类
     * @return 错误结果
     */
    @ExceptionHandler(BaseException.class)
    public ResponseResult<?> baseException(BaseException baseException) {
        log.error("异常信息 => {}", baseException.getMessage());
        return ResponseResult.fail(baseException.getMessage());
    }

}

