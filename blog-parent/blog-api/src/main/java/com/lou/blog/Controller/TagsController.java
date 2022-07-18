package com.lou.blog.Controller;

import com.lou.blog.service.TagService;
import com.lou.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lqw
 * @date 2022/4/8-4:18 下午
 */

@RestController //表示返回的是一个json格式的数据
@RequestMapping("tags")//路径映射
public class TagsController {
    @Autowired
    private TagService tagService;
    // /tags/hot
    @GetMapping("hot")
    public Result hot(){
        int limit=6;//返回最热的 6个标签
        return tagService.hots(limit);
    }



    @GetMapping
    public Result findAll(){
        /**
         * 查询所有的文章标签
         * @return
         */
        return tagService.findAll();
    }


    @GetMapping("detail")
    public Result findAllDetail(){
        /**
         * 查询所有的文章标签
         * @return
         */
        return tagService.findAllDetail();
    }



    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        /**
         * 查询该标签id下的所有文章
         * @return
         */
        return tagService.findDetailById(id);


    }





}