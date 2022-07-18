package com.lou.blog.utils;

import org.springframework.security.core.AuthenticationException;

/**
 * Created with IntelliJ IDEA.
 * User: gui
 * Date: 2021-08-02
 * Time: 下午 12:46
 * Description: 登录错误异常
 * @author gui
 */
public class CustomException extends AuthenticationException {

    public CustomException(String msg) {
        super(msg);
    }

}