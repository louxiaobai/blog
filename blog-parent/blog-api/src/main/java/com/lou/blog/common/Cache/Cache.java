package com.lou.blog.common.Cache;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {


    //过期时间
    long expire() default 1 * 60 * 1000;
    //缓存标识
    String name() default "";


}

