package com.lou.blog.service;

import com.lou.blog.pojo.SysUser;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lqw
 * @date 2022/4/11-4:22 下午
 */
@Transactional
public interface LoginService {




    /**
     * 登陆功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 检查token的合法性
     * @param token
     * @return
     */
    SysUser checkToken(String token);


    /**
     * 推出登陆 --注销token
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
