package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

/**
 * Created by wycm on 2018/10/13.
 */
@Data
public class Education {
    private Topic school;

    private Topic major;

    private Integer diploma;
}
