package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by wycm on 2018/10/21.
 * 答案
 */
@Data
@Document(collection="ZhihuAnswer")
public class Answer {
    @Id
    private String id;

    private String type;

    private Question question;

    private Author author;

    private String url;

    private String thumbnail;

    private Boolean is_collapsed;

    private Long created_time;

    private Long updated_time;

    private String extras;

    private Boolean is_copyable;

    private Boolean is_normal;

    private Integer voteup_count;

    private Integer comment_count;

    private Boolean is_sticky;

    private Boolean admin_closed_comment;

    private String comment_permission;

    private String reshipment_settings;

    private String content;

    private String editable_content;

    private String excerpt;

    private String collapsed_by;

    private String collapse_reason;

    private Date createTime;

    private Date updateTime;

}
