package com.lou.blog.service;

import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.ArticleParam;
import com.lou.blog.vo.params.PageParams;

/**
 * @author lqw
 * @date 2022/4/8-4:32 下午
 */
public interface ArticleService {

    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 首页最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 首页最新文章
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     * 首页显示文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    /**
     * 文章模糊查询操作
     * @param search
     * @return
     */
    Result searchArticle(String search);
}
