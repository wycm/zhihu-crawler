package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

/**
 * Created by wycm on 2018/10/13.
 */
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
