package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

import java.util.List;

//认证
@Data
public class Badge {
    private String type;
    private String description;

    List<Topic> topics;
}
