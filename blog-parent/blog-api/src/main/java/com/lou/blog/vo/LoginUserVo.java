package com.lou.blog.vo;

import lombok.Data;

@Data
public class LoginUserVo {
    //与页面交互

    private String id;

    private String account;//用户帐号

    private String nickname;//用户别名

    private String avatar;//用户的头像
}

