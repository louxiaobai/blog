package com.lou.blog.vo;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class CategoryVo {

//id，图标路径，图标名称

    private String id;

    private String avatar;

    private String categoryName;

    private String description;
}

