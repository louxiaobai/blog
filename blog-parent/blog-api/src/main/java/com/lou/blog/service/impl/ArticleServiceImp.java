package com.lou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lou.blog.dao.dos.Archives;
import com.lou.blog.dao.mapper.ArticleBodyMapper;
import com.lou.blog.dao.mapper.ArticleMapper;
import com.lou.blog.dao.mapper.ArticleTagMapper;
import com.lou.blog.pojo.Article;
import com.lou.blog.pojo.ArticleBody;
import com.lou.blog.pojo.ArticleTag;
import com.lou.blog.pojo.SysUser;
import com.lou.blog.service.*;
import com.lou.blog.utils.UserThreadLocal;
import com.lou.blog.vo.*;
import com.lou.blog.vo.params.ArticleParam;
import com.lou.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lqw
 * @date 2022/4/8-4:35 下午
 */
@Service
public class ArticleServiceImp  implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleTagMapper articleTagMapper;


    /**
     * 返回文章列表
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {

        System.out.println(pageParams.getPage());


        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());

        return Result.success(copyList(articleIPage.getRecords(),true,true));


    }
    /**
     * 返回文章列表
     * @param pageParams
     * @return
     */
//    @Override
//    public Result listArticle(PageParams pageParams) {
//
//        /**
//         * 分页查询article数据库表:得到需要的结果
//         */
//        //mybatisplus中的page
//        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
//
//
//        if (pageParams.getCategoryId()!=null){
//
//            //相当于 and category_id=#{categoryById}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//
//        }
//
//        List<Long> articleIdList=new ArrayList<>();
//        if (pageParams.getTagId()!=null){
//
//             //加入标签查询条件
//             //article表中并没有tag字段,一篇文章中有多个标签
//             //article_tag article_id 1:n tag_id-在表中是一对多的关系
//
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
//
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,pageParams.getTagId());
//
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//
//            if (articleIdList.size()>0){
//                //and id in{1,2,3   }
//                queryWrapper.in(Article::getId,articleIdList);
//
//            }
//        }
//
//
//        //1.按照创建时间进行降序排序
//        //2.按照是否置顶进行排序
//        //使用的mybatisplus自带的分页工具
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        //这里records是取得的数据库中的数据,而我们需要的是经常处理的数据
//         List<ArticleVo> articleVoList=copyList(records,true,true);
//
//         //返回对数据进行封装过后的数据
//        return Result.success(articleVoList);
//    }

    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(int limit) {

        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);//安装访问量进行排序
        queryWrapper.select(Article::getId,Article::getTitle);//只取id 和 title
        queryWrapper.last("limit "+limit);//只取出查询结果的前limit条数据
        //select id,title from article order by view_counts desc limit 5;

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));


    }

    /**
     * 首页最新文章
     * @param limit
     * @return
     */
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);//按照创建时间进行排序
        queryWrapper.select(Article::getId,Article::getTitle);//只取id 和 title
        queryWrapper.last("limit "+limit);//只取出查询结果的前limit条数据
        //select id,title from article order by getCreateDate desc limit 5;
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public Result listArchives() {

      List<Archives>  archivesList= articleMapper.listArchives();

        return Result.success(archivesList);
    }

    /**
     * 1.根据文章id 查找文章内容
     * 2.根据bodyId和categoryid 去查询关联标
     * @param articleId
     * @return
     */
    @Override
    public Result findArticleById(Long articleId) {

        //根据文章id获取到文章的内容
        Article article = this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看完文章,就进行增加阅读量,有没有问题呢
        //查看完文章后,本应该直接返回数据了,这时候做一个更新操作,更新是加写锁的,阻塞其他的阅读进程,性能会比较低---无法降低消耗时间
        //更新增加了此次接口的耗时--如果一旦出现更新问题,不能够影响查看文章的操作
        //使用线程池-可以把更新操作扔到线程池中执行,这样和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    /**
     * 文章发布
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {

        //此接口 要加入到登陆拦截当中-减少redis的压力 --通过UserThreadLocal获取用户
        SysUser sysUser = UserThreadLocal.get();


        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        boolean isEdit = false;
        if (articleParam.getId() != null){
            article = new Article();
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true;
        }else{
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            //插入之后 会生成一个文章id
            this.articleMapper.insert(article);
        }
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                if (isEdit){
                    //先删除
                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(ArticleTag::getArticleId,articleId);
                    articleTagMapper.delete(queryWrapper);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        if (isEdit){
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            LambdaUpdateWrapper<ArticleBody> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody, updateWrapper);
        }else {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            articleBodyMapper.insert(articleBody);

            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());

        if (isEdit){
            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(article.getId());
//            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
        }
        return Result.success(map);
    }

    /**
     * 文章模糊查询
     * @param search
     * @return
     */
    @Override
    public Result searchArticle(String search) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.like(Article::getTitle,search);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));

    }

    //数据封装工具-对多个数据进行封装操作
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {

        List<ArticleVo> articleVoList=new ArrayList<>();

        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }


    //数据封装工具-对多个数据进行封装操作--方法重载入
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }
    //数据封装工具--对单个数据进行设置
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd:HH:mm"));
        //并不是所有的标签和作者信息都要进行展示的
        //在这里进行判断是否需要标签和个人信息的展示
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            SysUser sysUser = sysUserService.findUserById(authorId);

            UserVo userVo = new UserVo();

            userVo.setAvatar(sysUser.getAvatar());

            userVo.setId(sysUser.getId().toString());

            userVo.setNickname(sysUser.getNickname());

            articleVo.setAuthor(userVo);
        }
        //文章内容
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        //文章类别
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryByid(categoryId));
        }
        return articleVo;

    }
    /**
     * 根据 bodyId查询 bodyContent 内容
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticleBodyById(Long bodyId) {

        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;

    }


}