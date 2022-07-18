package com.lou.blog.service;

import com.lou.blog.pojo.SysUser;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.UserVo;

/**
 * @author lqw
 * @date 2022/4/9-8:53 下午
 */
public interface SysUserService {

    /**
     * 根据用户ID获获取到用户
     * @param id
     * @return
     */
    UserVo findUserVoById(Long id);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    SysUser findUserById(long id);

    /**
     * 根据帐号密码查询用户
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token查找用户信息
     * @param token
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查询数据库中是已经存在该账户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户信息
     * @param sysUser
     */
    void save(SysUser sysUser);
}
