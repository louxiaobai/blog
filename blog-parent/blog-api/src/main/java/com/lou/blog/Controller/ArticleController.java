package com.lou.blog.Controller;

import com.lou.blog.common.Cache.Cache;
import com.lou.blog.common.aop.LogAnnotation;
import com.lou.blog.service.ArticleService;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.ArticleParam;
import com.lou.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqw
 * @date 2022/4/8-4:18 下午
 */

//json数据进行交互


@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加入此注解 代表要对此接口进行记录日志操作
    //加入缓存机制
//    @Cache(expire = 5*60*1000,name="listArtilce ")
    @LogAnnotation(moudle = "文章",operator = "获取文章列表")
    public Result listArtilce(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页最热文章
     * @return
     */
    //加入缓存机制
//    @Cache(expire = 5*60*1000,name="hot_article")
    @PostMapping("hot")
    public Result hotArtilce(){

        int limit=5;
        return articleService.hotArticle(5);
    }
    /**
     * 首页最新文章
     * @return
     */
    @PostMapping("new")
    public Result newArtilce(){

        int limit=5;
        return articleService.newArticle(5);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){

        return articleService.listArchives();
    }


    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){

        return articleService.findArticleById(articleId);

    }

    /**
     * 文章发布
     * @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
     * 而最常用的使用请求体传参的无疑是POST请求了，所以使用@RequestBody接收数据时，一般都用POST方式进行提交。
     * @param articleParam
     * @return
     */

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }

    /**
     * 进行文章修改的时候 进行查询到文章的内容
     * @param articleId
     * @return
     */
    @PostMapping("{id}")
    public Result articleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);

    }

    /**
     * 进行模糊查询操作
     * @param articleParam
     * @return
     */
    @PostMapping("search")
    public Result search(@RequestBody ArticleParam articleParam){
        //写一个搜索接口
        String search = articleParam.getSearch();
        return articleService.searchArticle(search);
    }





}