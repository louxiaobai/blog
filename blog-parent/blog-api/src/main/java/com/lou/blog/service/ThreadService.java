package com.lou.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lou.blog.dao.mapper.ArticleMapper;
import com.lou.blog.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author lqw
 * @date 2022/4/14-5:50 下午
 */
@Component
public class ThreadService {

    /**
     * 期望此操作在线程池中执行，不会影响原有的主线程
     * @param articleMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {


        //1.拿到原有的viewCount
        int viewCounts = article.getViewCounts();
        Article articleUpdate=new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaQueryWrapper<Article> updateWrapper=new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程的环境下,线程安全--乐观锁 如果操作的时候和期望的阅读量不一样 会修改失败
        updateWrapper.eq(Article::getViewCounts,viewCounts);

        //update article  set view_count=100 where view=99,and id=11
        articleMapper.update(articleUpdate, updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println("更新完成了～");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}