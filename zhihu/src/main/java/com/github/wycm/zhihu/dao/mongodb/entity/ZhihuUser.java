package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection="ZhihuUser")
@Data
public class ZhihuUser {
    @Id
    private String id;

    //首页url
    private String url;

    private Boolean is_followed;

    private String avatar_url_template;

    private Integer following_count;

    private List<Location> locations;

    private Integer answer_count;

    private String user_type;

    private Boolean is_following;

    private  List<Education> educations;

    private String url_token;

    private String question_count;

    private String voteup_count;

    private Topic business;

    private String headline;

    private List<Badge> badge;

    private String name;

    private Boolean is_advertiser;

    private String avatar_url;

    private Boolean is_org;

    private Integer thanked_count;

    private Integer gender;

    private Integer follower_count;

    private List<Map<String, Topic>> employments;

    private Integer articles_count;

    private String type;

    private Date createTime;

    private Date updateTime;
}
