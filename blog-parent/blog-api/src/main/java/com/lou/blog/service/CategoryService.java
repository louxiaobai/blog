package com.lou.blog.service;

import com.lou.blog.vo.CategoryVo;
import com.lou.blog.vo.Result;

import java.util.List;

/**
 * @author lqw
 * @date 2022/4/14-4:20 下午
 */
public interface CategoryService {

    /**
     * 根据文章id查找文章分类
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryByid(Long categoryId);

    /**
     * 进行查询所以的文章类别
     * @return
     */
    Result findAll();



    Result findAllDetail();

    /**
     * 根据分类id查询具体的分类内容
     * @param id
     * @return
     */
    Result findAllDetailById(Long id);
}