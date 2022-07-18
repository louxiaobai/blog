package com.lou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lou.blog.dao.mapper.SysUserMapper;
import com.lou.blog.pojo.SysUser;
import com.lou.blog.service.LoginService;
import com.lou.blog.service.SysUserService;
import com.lou.blog.vo.LoginUserVo;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lqw
 * @date 2022/4/9-8:54 下午
 */

@Service
public class SysUserServiceImp implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;



    @Autowired
    private LoginService loginService;


    @Override
    public UserVo findUserVoById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);

        //防止空指针的出现
        if (sysUser==null){

            sysUser=new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/face.png");
            sysUser.setNickname("神拐子");

        }

        UserVo userVo=new UserVo();

        userVo.setId(String.valueOf(sysUser.getId()));

        BeanUtils.copyProperties(sysUser, userVo);

        return userVo;
    }

    /**
     * 根据 id查询用户
     * @param id
     * @return
     */
    @Override
    public SysUser findUserById(long id) {

        SysUser sysUser = sysUserMapper.selectById(id);

        //防止空指针的出现
        if (sysUser==null){

            sysUser=new SysUser();
            sysUser.setNickname("神拐子");

        }
        return sysUser;
    }

    /**
     * 根据帐号密码查询用户是否存在
     * @param account
     * @param password
     * @return
     */
    @Override
    public SysUser findUser(String account, String password) {

        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(SysUser::getAccount,account);

        queryWrapper.eq(SysUser::getPassword,password);;

        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);

        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 根据token查找用户信息
     * @param token
     */
    @Override
    public Result findUserByToken(String token) {
         
        /**
         * 1.token合法性校验
         *  是否为空,解析是否成功  redis是否存在
         *
         * 2.如果校验失败 返回错误
         *
         * 3.如果校验成功 返回对应的结果 LoginUserVo
         *
         */

        //判断

        SysUser sysUser=loginService.checkToken(token);

        if (sysUser==null){
            return null;
        }

        LoginUserVo loginUserVo=new LoginUserVo();

        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());

        loginUserVo.setNickname(sysUser.getNickname());

        return Result.success(loginUserVo);

    }

    /**
     * 判断账户是否存在
     * @param account
     * @return
     */
    @Override
    public SysUser findUserByAccount(String account) {

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper();

        lambdaQueryWrapper.eq(SysUser::getAccount,account);

        //确保只查询到一条数据
        lambdaQueryWrapper.last("limit 1");

        return sysUserMapper.selectOne(lambdaQueryWrapper);



    }

    /**
     * 保存用户新信息
     * @param sysUser
     */
    @Override
    public void save(SysUser sysUser) {

        //保存用户 id 会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //mybatis-plus
        this.sysUserMapper.insert(sysUser);

    }

}