package com.lou.blog.Controller;

import com.lou.blog.service.SysUserService;
import com.lou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqw
 * @date 2022/4/11-5:52 下午
 */
@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    //通过头部传递信息相对更加的安全
    @GetMapping("currentUser")
    public Result currenUser(@RequestHeader("Authorization") String token){

        return sysUserService.findUserByToken(token);

    }
}