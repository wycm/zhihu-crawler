package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by wycm on 2018/10/21.
 */
@Data
@Document(collection = "ZhihuArticle")
public class Article {
    private Long updated;

    private Author author;

    private Long created;

    private String url;

    private String title;

    private String excerpt;

    private String content;

    private Integer comment_count;

    private String excerpt_title;

    private String type;

    @Id
    private String id;

    private Long voteup_count;

    private Date createTime;

    private Date updateTime;
}
