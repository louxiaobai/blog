package com.lou.blog.pojo;

import lombok.Data;


//类别表

@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}

