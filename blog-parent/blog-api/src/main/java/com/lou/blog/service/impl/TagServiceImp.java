package com.lou.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lou.blog.dao.mapper.TagMapper;
import com.lou.blog.pojo.Tag;
import com.lou.blog.service.TagService;
import com.lou.blog.vo.Result;
import com.lou.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lqw
 * @date 2022/4/9-8:18 下午
 */
@Service
public class TagServiceImp implements TagService {


    @Autowired
    private TagMapper tagMapper;

    /**
     * 返回标签列表
     * @param articleId
     * @return
     */
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {

      //mybatisplus 不支持多表查询,这表自己写一个查询接口
      List<Tag> tags=tagMapper.findTagsByArticleId(articleId);

        return copyList(tags);
    }



    /**
     * 对多条数据进行处理
     * @param tagList
     * @return
     */
    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;

    }

    /**
     * 数据处理
     * @param tag
     * @return
     */
    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);

        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }


    /**
     * 返回最热标签--前n天
     * 最热标签:拥有文章数量最多的为最热标签
     * 查询 根据tag_id 分组 计数,从大到小排列 取 前limit 个
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {


        List<Long> tagIdList = tagMapper.findHotsTagIds(limit);

        //sql语句那个in 是不能够为空的
        if(CollectionUtils.isEmpty(tagIdList)){
            return Result.success(Collections.emptyList());
        }
        //这里需要的是tag_id 和 tagName   是一个tog对象
        //select * from tag where id in (1,2,3,4,5)

        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIdList);

        return Result.success(tagList);
    }

    /**
     * 查询所有标签
     * @return
     */
    @Override
    public Result findAll() {


        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tagList));
    }

    /**
     * 进行查询所有标签--全部
     * @return
     */
    @Override
    public Result findAllDetail() {
        List<Tag> tagList = this.tagMapper.selectList(new LambdaQueryWrapper<>());


        return Result.success(copyList(tagList));
    }

    /**
     * 根据标签id 查询 标签的详细内容
     * @param id
     * @return
     */
    @Override
    public Result findDetailById(Long id) {

        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }

}