package com.lou.blog.pojo;

import lombok.Data;

@Data
public class Comment {

    private Long id;

    private String content;//评论内容

    private Long createDate;//评论时间

    private Long articleId;//文章的id

    private Long authorId;//用户的id

    private Long parentId;//父级评论

    private Long toUid;//是那个用户的评论-如果是二级评论则这个值就是一级评论的id

    private Integer level;//评论级别--一级和二级


}

