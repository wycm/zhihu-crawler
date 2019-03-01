package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;


@Data
public class Location {
    private String introduction;

    private String avatar_url;

    private String name;

    private String url;

    private String type;

    private String excerpt;

    private String id;
}
