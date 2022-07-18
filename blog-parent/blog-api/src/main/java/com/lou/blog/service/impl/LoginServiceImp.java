package com.lou.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.lou.blog.pojo.SysUser;
import com.lou.blog.service.LoginService;
import com.lou.blog.service.SysUserService;
import com.lou.blog.utils.JWTUtils;
import com.lou.blog.vo.ErrorCode;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lqw
 * @date 2022/4/11-4:25 下午
 */
@Service//注入到spring容器当中

public class LoginServiceImp implements LoginService {


    /**
     * 但是我们如果仅仅使用MD5加密，却会导致密文被破译，这种破译并不是什么高深的方法（并不代表MD5加密方式的不安全），
     * 其原理是建立一个庞大的数据库，收集明文及密文的对应关系，然后检索撞出结果。我们可以在CMD5这个网站破译简单的MD5密文。
     * 为了应对这种状况，我们可以使用加盐的方法，设一个盐值（字符串），将其加到明文或密文上，然后进行加密，
     * 越复杂（例如多次MD5加密）安全性越高，被撞开的几率就越低。
     */

    private static final String slat = "mszlu!@#";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired

    private SysUserService sysUserService;

    /**
     * 1.检查参数是否合法
     * 2.根据用户名和密码取user表中查询 是否存在
     * 3.如果不存在 则返回登陆失败
     * 如果存在,使用jwt 生成token 返回给前端
     * 5.token 放入redis中  token:user信息 设置过期时间
     * (登陆认证的时候,先认真token字符串是否合法,然后取redis中认证是否存在)
     *
     */
    @Override
    public Result login(LoginParams loginParams) {

        String account = loginParams.getAccount();

        String password = loginParams.getPassword();

        //第一步 判断账户和密码舒服合法--不合法则直接进行返回
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)){

            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());

        }
        //对前端传过来的密码数据进行加密(密码+密码盐)
        password= DigestUtils.md5Hex(password+slat);
        //第二步---进行查找数据库如果数据库中没有则直接进行返回
        SysUser sysUser= sysUserService.findUser(account,password);
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //第三步-生成token 将该用户的token 以及对用的用户信息存储到redis中
        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),100, TimeUnit.DAYS);

        return Result.success(token);
    }

    /**
     * 对token 进行检测
     * @param token
     * @return
     */
    @Override
    public SysUser checkToken(String token) {

        if (StringUtils.isBlank(token)){

            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map==null){
            return null;
        }
        //需要从根据token 从缓存中提起到 用户信息
        String userJson=redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(userJson)){//说明token过期了
            System.out.println("token过期了");
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /**
     * 推出登陆
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {

        //进行删除token
        redisTemplate.delete("TOKEN_"+token);

        return Result.success(null);
    }

    /**
     * 登陆功能
     * @param loginParams
     * @return
     */
    @Override
    public Result register(LoginParams loginParams)     {

        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在,如果存在 返回账户已经被注册
         * 3.不存,进行注册用户
         * 4.生成token
         * 5.将生成的token存储到redis中 设置成token:user信息 并且设置过期时间
         * :使用redis这个非关系型数据库,相当于有个缓存作用,减少对mysql数据库的访问,并且提高安全性
         * 6.注意加入事务,一旦中间的任何过程出现问题,注册的用户需要回滚
         */

//          1.判断参数是否合法
        String account=loginParams.getAccount();

        String password = loginParams.getPassword();

        String nickname = loginParams.getNickname();

        //判断数据是否有误,从前端接收的参数有误则直接进行返回
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
//          2.判断账户是否存在,如果存在 返回账户已经被注册---从数据库中查询数据
        SysUser sysUser=sysUserService.findUserByAccount(account);

        if (sysUser!=null){//数据数据已经存在直接进行返回不进行注册操作

            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

//      3.不存在,进行注册用户
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        //登陆盐 要注意
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/face.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt(slat);//设置加密盐
        sysUser.setStatus("");
        sysUser.setEmail("");
        //进行保存用户
        this.sysUserService.save(sysUser);
//       4.生成token
        String token = JWTUtils.createToken(sysUser.getId());

//          5.将token添加到redis中
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),100, TimeUnit.DAYS);
//          6.注意加入事务,一旦中间的任何过程出现问题,注册的用户需要回滚--事务加到了接口上了Transctional
        return Result.success(token);

    }
}