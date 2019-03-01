package com.github.wycm.zhihu.dao.mongodb.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection="TopicActivity")
public class TopicActivity {
    private String attached_info;

    private String type;

    private Target target;

    private String target_description;

    private Date createTime;

    private Date updateTime;

    //delete时必须要
    private String id;
}
