package com.lou.blog.vo.params;

import lombok.Data;

@Data
public class CommentParam {


    private Long articleId;//文章id

    private String content;//评论内容

    private Long parent;//父节点

    private Long toUserId;//给谁进行评论的
}

