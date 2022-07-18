package com.lou.blog.config;
import com.lou.blog.Handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lqw
 * @date 2022/4/7-5:56 下午
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    //配置拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;
    // 解决跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**").allowedOriginPatterns("http://localhost:9998","http://47.115.218.208:9998");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口,后续实际遇到拦截的接口时候,在配置真正的拦截接口
        //进行评论的时候必须登陆 才能够进行评论
        registry.addInterceptor(loginInterceptor).addPathPatterns("/test")
                                                 .addPathPatterns("/comments/create/change")
                                                 .addPathPatterns("/articles/publish");
    }
}