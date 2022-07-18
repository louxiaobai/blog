package com.lou.blog.service;

import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.CommentParam;

/**
 * @author lqw
 * @date 2022/4/15-3:42 下午
 */
public interface CommentService {


    /**
     * 根据文章id查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentByArticleId(Long articleId);

    /**
     * 进行评论的功能
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
