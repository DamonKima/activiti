package com.example.activiti8test.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum HttpCodeEnum {
    //==================== 登录相关枚举 ======================
    /**
     * 登陆超时
     */
    USER_LOGIN_TIME_OUT(100, "登陆超时"),
    /**
     * 用户未登录
     */
    USER_NOT_LOGIN(101, "用户未登录，请先进行登录"),
    /**
     * 账户被禁用，请联系管理员解决
     */
    ACCOUNT_IS_DISABLED(102, "账户被禁用，请联系管理员解决"),
    /**
     * 用户信息加载失败
     */
    USER_INFO_LOAD_FAIL(103, "用户信息加载失败"),
    /**
     * 用户身份信息获取失败
     */
    USER_IDENTITY_LOAD_FAIL(104, "用户身份信息获取失败"),
    /**
     * 用户名不能为空
     */
    USERNAME_CAN_NOT_BE_EMPTY(105, "用户名不能为空"),
    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(106, "用户名或密码错误"),
    /**
     * 用户登录成功
     */
    USER_LOGIN_SUCCESS(108, "用户登录成功"),
    /**
     * 用户注销成功
     */
    USER_LOGOUT_SUCCESS(109, "用户注销成功"),
    //==================== 注册相关枚举 ======================
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(300, "验证码错误"),
    /**
     * 验证码过期
     */
    CAPTCHA_ALREADY_EXPIRE(301, "验证码已过期"),
    /**
     * 用户名已存在
     */
    USERNAME_ALREADY_EXISTED(302, "用户名已存在"),
    /**
     * 参数格式不合法
     */
    PARAMETER_VALID_ERROR(600, "参数格式不合法"),
    //======================= 其他枚举 ==============================
    /**
     * 没有权限
     */
    PERMISSION_NOT_DEFINED(403, "您没有操作权限"),
    /**
     * 操作成功
     */
    OPERATOR_IS_SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    OPERATOR_IS_FAILED(500, "操作失败"),
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(600, "未知异常");


    /**
     * 状态码
     */
    private final int code;
    /**
     * 返回信息
     */
    private final String message;

}
