package com.lou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lou.blog.dao.mapper.CategoryMapper;
import com.lou.blog.pojo.Category;
import com.lou.blog.service.CategoryService;
import com.lou.blog.vo.CategoryVo;
import com.lou.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lqw
 * @date 2022/4/14-4:20 下午
 */

@Service
public class CategoryServiceImp implements CategoryService {


    public static void main(String[] args) {

        int[] score=new int[]{5,4,3,2,1};





    }


    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryByid(Long categoryId) {

        Category category = categoryMapper.selectById(categoryId);

        CategoryVo categoryVo = new CategoryVo();

        BeanUtils.copyProperties(category,categoryVo);

        categoryVo.setId(String.valueOf(category.getId()));

        return categoryVo;
    }

    /**
     * 实现查询文章类别
     * @return
     */
    @Override
    public Result findAll() {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.select(Category::getId,Category::getCategoryName);

        List<Category> categories=categoryMapper.selectList(queryWrapper);

        //返回页面交互的对象
        return Result.success(copyList(categories));
    }

    /**
     * 查询文章类别 全部详情
     * @return
     */
    @Override
    public Result findAllDetail() {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        List<Category> categories=categoryMapper.selectList(queryWrapper);

        //返回页面交互的对象
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetailById(Long id) {

        Category category = categoryMapper.selectById(id);

        return Result.success(copy(category));
    }


    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);

        categoryVo.setId(String.valueOf(category.getId()));
//        //id不一致要重新设立
//        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();


        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}