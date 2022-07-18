package com.lou.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lou.blog.dao.dos.Archives;

import com.lou.blog.pojo.Article;

import java.util.List;

/**
 * @author lqw
 * @date 2022/4/8-4:11 下午
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章归档
     * @return
     */
    List<Archives> listArchives();


    /**
     * 按照条件查找文章
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);


}
