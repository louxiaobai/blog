package com.lou.blog.Controller;


import com.lou.blog.pojo.SysUser;
import com.lou.blog.utils.UserThreadLocal;
import com.lou.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){

        //得到用户信息

        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}

