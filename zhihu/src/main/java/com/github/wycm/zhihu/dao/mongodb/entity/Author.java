package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by wycm on 2018/10/16.
 */
@Data
public class Author {
    private String avatar_url_template;
    private List<Badge> badge;
    private String name;
    private EduMemberTag edu_member_tag;
    private String headline;
    private String url_token;
    private String user_type;
    private Boolean is_advertiser;
    private String avatar_url;
    private boolean is_following;
    private boolean is_org;
    private int gender;
    private String url;
    private String type;
    @Id
    private String id;
}
