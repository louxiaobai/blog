package com.lou.blog.Controller;

import com.lou.blog.service.LoginService;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lqw
 * @date 2022/4/12-10:47 下午
 */
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParams loginParams){

        //sso单点登陆,后期如果吧登陆注册功能 提出去(单独的服务,可以独立提供接口服务)
        return loginService.register(loginParams);


    }

}