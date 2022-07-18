package com.lou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lou.blog.dao.mapper.CommentMapper;
import com.lou.blog.pojo.Comment;
import com.lou.blog.pojo.SysUser;
import com.lou.blog.service.CommentService;
import com.lou.blog.service.SysUserService;
import com.lou.blog.utils.UserThreadLocal;
import com.lou.blog.vo.CommentVo;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.UserVo;
import com.lou.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lqw
 * @date 2022/4/15-3:42 下午
 */
@Service
public class CommentServiceImp implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 根据文章id查询所有的评论列表
     * @param articleId
     * @return
     */
    @Override
    public Result commentByArticleId(Long articleId) {

        /**
         * 1.根据文章id 查询 评论列表 从comment 表中查询
         * 2.根据作者的id查询作者信息
         * 3. 判断 如果 level = 1 要去查询它有没有子评论
         * 4. 如果有 根据评论id 进行查询 （parent_id）
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据文章id进行查询
        queryWrapper.eq(Comment::getArticleId,articleId );
        //根据层级关系进行查询---进行查询一级评论-一级评论可能有多个
        queryWrapper.eq(Comment::getLevel,1 );
        //将一级评论存储到一个集合中
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        List<CommentVo> commentVoList = copyList(comments);

        return Result.success(commentVoList);
    }

    /**
     * 增加评论的功能
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {

        //进行评论的时候需要根据缓存得到用户的基本信息
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());

        Long parent = commentParam.getParent();//判断是否有父亲节点
        if (parent == null || parent == 0) {
            comment.setLevel(1);//如果父节点为空则说明是一级别评论
        }else{
            comment.setLevel(2);
        }
        //如果是空，parent就是0
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);

        CommentVo commentVo = copy(comment);

        return Result.success(commentVo);

//        return Result.success(null);
    }

    //对list表中的comment进行判断
    private List<CommentVo> copyList(List<Comment> comments) {

        //创建一个CommentVo存储 评论信息
        List<CommentVo> commentVoList=new ArrayList<>();

        //存储每一个一级评论以及一级评论下的二级评论
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {

        //创建一个CommentVo对象
        CommentVo commentVo=new CommentVo();
        /**
         * BeanUtils.copyProperties(a, b);
         *
         * b中的存在的属性，a中一定要有，但是a中可以有多余的属性；
         * a中与b中相同的属性都会被替换，不管是否有值；
         * a、 b中的属性要名字相同，才能被赋值，不然的话需要手动赋值；
         * Spring的BeanUtils的CopyProperties方法需要对应的属性有getter和setter方法；
         * 如果存在属性完全相同的内部类，但是不是同一个内部类，即分别属于各自的内部类，则spring会认为属性不同，不会copy；
         * spring和apache的copy属性的方法源和目的参数的位置正好相反，所以导包和调用的时候都要注意一下。
         */
        //将comment的值copy到commentVo中
        BeanUtils.copyProperties(comment, commentVo);
        //转成String类型防止精度丢失
        commentVo.setId(String.valueOf(comment.getId()));
        //得到用户的信息
        Long authorId = comment.getAuthorId();
        //查询到评论用户的基本信息
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        //将值赋值给----commentVo
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();

        if (level==1){//1级别评论 说明可能有子评论

            Long id = comment.getId();//
            //找到该条评论下的子评论 并且封装成Comment 对象
            List<CommentVo> commentVoList=findCommentsByParentId(id);
            //设置子评论
            commentVo.setChildrens(commentVoList);

        }
        //to user 意思为给谁进行评论的--意思为二级的评论
        if (level>1){

            //这条评论是给谁评论的
            Long toUid = comment.getToUid();

            //或得该评论上一级的评论信息
            UserVo toUserVo=this.sysUserService.findUserVoById(toUid);

            //将评论的对象进行赋值操作
            commentVo.setToUser(toUserVo);
        }
        //返回commetVo
        return commentVo;
    }

    /**
     * 根据父亲节点的id查询评论数
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {

        //找到全部的属于这个评论下的二级别评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Comment::getParentId,id);

        queryWrapper.eq(Comment::getLevel,2);

        List<Comment> comments = this.commentMapper.selectList(queryWrapper);

        //将二级评论进行包装后返回
        return copyList(comments);

    }
}