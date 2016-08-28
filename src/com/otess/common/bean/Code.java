package com.otess.common.bean;

public class Code {
	/**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 请求格式出错。(包括表达参数，处理参数错误)
     */
    public static final int FAIL = 400;

    /**
     * 服务器错误
     */
    public static final int ERROR = 500;

    /**
     * 资源不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * 认证授权失败
     */
    public static final int TOKEN_INVALID = 401;

    /**
     * 已经注册过了
     */
    public static final int ACCOUNT_EXISTS = 614;
    /**
     * http请求方法错误
     */
    public static final int METHOD_ERROR=405;
    /**
     * 数量已经达到上限，无法注册了
     */
    public static final int MUCH_ERROR=630;
}
