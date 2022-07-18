package com.lou.blog.Handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lou.blog.pojo.SysUser;
import com.lou.blog.service.LoginService;
import com.lou.blog.utils.UserThreadLocal;
import com.lou.blog.vo.ErrorCode;
import com.lou.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lqw
 * @date 2022/4/12-11:31 下午
 */
@Component
@Slf4j //日志工具
public class LoginInterceptor  implements HandlerInterceptor {

    @Autowired
    LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //在执行Controller方法(handler) 之前执行
        /**
             * 1.需要判断请求的接口路径 是否为HandlerMethod(controller)方法
             * 2.判断token是否为空,如果为空 则表示未登陆
             * 3.如果token不为空,登陆验证 loginService checkToken
             * 4.如果认证成功放心即可
         */
        //如果不是我们的方法进行放行--判断是否是Controller方法,如果不是Controller方法则直接进行放行
        if (!(handler instanceof HandlerMethod)){
            //handler 可能是 RequestResourceHandler springboot 程序 访问静态资源 默认去classpath下的static目录去查询
            return true;
        }
        //从浏览器的请求头中获取到Authorization--token信息--登陆的时候存储在localSorage中的
        //jwt 生成的密钥
        String token = request.getHeader("Authorization");
        System.out.println("toke来了啊"+token);
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");


        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            //设置浏览器识别返回的是json
            response.setContentType("application/json;charset=utf-8");
            //https://www.cnblogs.com/qlqwjy/p/7455706.html response.getWriter().print()
            //SON.toJSONString则是将对象转化为Json字符串
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        // 3.如果token不为空,登陆验证 loginService checkToken
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //4.如果认证成功放心即可
        //如何在controller中,直接进行获取用户信息-通过ThreadLocal
        UserThreadLocal.put(sysUser);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //用户用完需要进行删除,如果不进行删除会有内存泄露的风险
        UserThreadLocal.remove();//用完即删除

    }
}