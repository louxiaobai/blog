package com.lou.blog.service;

import com.lou.blog.vo.Result;
import com.lou.blog.vo.TagVo;

import java.util.List;

/**
 * @author lqw
 * @date 2022/4/9-8:16 下午
 */
public interface TagService {


    /**
     * 得到标签的列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 返回最热标签--前n条数据
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 查询所有文章标签
     * @return
     */
    Result findAll();


    /**
     * 查询文章所有标签--详细
     * @return
     */
    Result findAllDetail();

    /**
     * 查询文章标签的详细信息
     * @param id
     * @return
     */
    Result findDetailById(Long id);
}
