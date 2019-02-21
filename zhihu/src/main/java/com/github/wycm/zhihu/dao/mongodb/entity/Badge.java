package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by wycm on 2018/10/13.
 */
//认证
@Data
public class Badge {
    private String type;
    private String description;

    List<Topic> topics;
}
