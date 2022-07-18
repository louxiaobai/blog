package com.lou.blog.Controller;

import com.lou.blog.service.LoginService;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqw
 * @date 2022/4/11-4:20 下午
 */

@RestController
@RequestMapping("login")
public class LoginController {

        @Autowired
        private LoginService loginService;

        @PostMapping
        public Result login(@RequestBody LoginParams loginParams) {

            //进行登陆验证客户
            return loginService.login(loginParams);

        }
}