package com.lou.blog.Controller;

import com.lou.blog.service.CommentService;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqw
 * @date 2022/4/15-3:39 下午
 */

@RestController
@RequestMapping("comments")
public class CommentControl {


    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentService.commentByArticleId(articleId);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }

}