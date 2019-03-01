package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;

@Data
public class Target {
    private Author author;

    private String url;

    private Question question;

    private String excerpt;

    private Long updated_time;

    /**
     * 文章标题
     */
    private String title;

    private String content;

    private String thumbnail;

    private Integer comment_count;

    private Long created_time;

    /**
     * answer, article
     */
    private String type;

    private String id;

    private Integer voteup_count;

    /**
     * type为article时，会有以下字段
     */
    private Long updated;

    private Long created;
}
