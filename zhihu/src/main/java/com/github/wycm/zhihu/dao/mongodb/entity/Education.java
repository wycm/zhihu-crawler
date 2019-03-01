package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

@Data
public class Education {
    private Topic school;

    private Topic major;

    private Integer diploma;
}
