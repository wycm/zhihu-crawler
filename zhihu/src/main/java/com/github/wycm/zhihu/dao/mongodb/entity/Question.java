package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by wycm on 2018/10/16.
 */
@Data
@Document(collection="ZhihuQuestion")
public class Question {
    @Id
    private String id;

    private String title;

    private String detail;

    private String url;

    private Long created;

    private Boolean is_following;

    private String question_type;

    private String type;

    private Long updated_time;

    //以下为自定义属性

    private Date createTime;

    private Date updateTime;

    private Date answerUpdateTime;

    private String author;

}
