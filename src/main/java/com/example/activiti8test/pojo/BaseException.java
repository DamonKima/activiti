package com.example.activiti8test.pojo;

import com.example.activiti8test.enmu.HttpCodeEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * @projectName: Activiti8Test
 * @package: com.example.activiti8test.pojo
 * @className: BaseException
 * @author: KiMa
 * @description: 统一异常处理的目的是为了防止直接将后台的报错信息返回给前端，
 * 这样对于前端的同学并不友好，而且对于后端同学的排错也不方便。
 * 自定义异常类
 * 全局异常处理类
 * @date: 2023-12-12 16:49
 * @version: 1.0
 */
@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -463971436835019374L;
    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 报错信息
     */
    private final String message;

    /**
     * 全参构造方法
     *
     * @param code    状态码
     * @param message 报错信息
     */
    public BaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构造方法
     *
     * @param message 报错信息
     */
    public BaseException(String message) {
        this(HttpCodeEnum.OPERATOR_IS_FAILED.getCode(), message);
    }

    /**
     * 构造方法
     *
     * @param httpCodeEnum http枚举类
     */
    public BaseException(HttpCodeEnum httpCodeEnum) {
        this(httpCodeEnum.getCode(), httpCodeEnum.getMessage());
    }

}
