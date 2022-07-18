package com.lou.blog.Controller;

import com.lou.blog.service.CategoryService;
import com.lou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lqw
 * @date 2022/4/16-7:47 下午
 */
@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public Result category(){
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoryDetail(){
        return categoryService.findAllDetail();
    }



    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.findAllDetailById(id);
    }





}