package com.lou.blog.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    //防止精度丢失
//    @JsonSerialize(using = ToStringSerializer.class)
    private String  id;//文章 id

    private String title;//文章标题

    private String summary;//简介

    private Integer commentCounts;//评论数量

    private Integer viewCounts;//浏览量

    private Integer weight;//权重-是否置顶

    private String createDate;//创建时间

    private UserVo author;//文章作者

//    private String author;//文章作者

    private ArticleBodyVo body;//

    private List<TagVo> tags;

//    private List<CategoryVo> categorys;

    private CategoryVo category;//文章分类

}

