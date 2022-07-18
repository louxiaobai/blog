package com.lou.blog.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo  {

//    //这里注解 防止精度损失-把id专成为String
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;//

    private UserVo author;//存储用户的信息

    private String content;//评论内容

    private List<CommentVo> childrens;//包含的二级评论

    private String createDate;//时间

    private Integer level;//评论级别

    private UserVo toUser;//表示是给那个用户评论的
}

