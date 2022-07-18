package com.lou.blog.common.aop;

import java.lang.annotation.*;

/**
 * @author lqw
 * @date 2022/4/16-11:48 下午
 */
// type 代表可以放在类上面 METHOD 代表可以放在方法上面
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {


    String moudle() default "";

    String operator() default "";


}
