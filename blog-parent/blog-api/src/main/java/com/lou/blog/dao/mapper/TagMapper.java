package com.lou.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lou.blog.pojo.Tag;

import java.util.List;

/**
 * @author lqw
 * @date 2022/4/8-4:13 下午
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);
    /**
     * 查询最热 id 并且返回前 limit 条数据
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);
    /**
     * 返回tag 对象的集合
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
