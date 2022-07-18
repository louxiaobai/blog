package com.lou.blog.utils;


import com.lou.blog.pojo.SysUser;

import java.util.HashMap;

public class UserThreadLocal {

    private UserThreadLocal(){}



    //线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){

        LOCAL.set(sysUser);

    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}

