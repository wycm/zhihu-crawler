package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="ZhihuTopic")
@Data
public class Topic {
    private String introduction;

    private String avatar_url;

    private String name;

    private String url;

    private String type;

    private String excerpt;

    @Id
    private String id;
    /**
     * 以下为自定义属性
     */
    private Date createTime;

    private Date updateTime;

    //话题动态最后更新时间
    private Date topicActivityUpdateTime;
}
