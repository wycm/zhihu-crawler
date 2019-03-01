package com.github.wycm.zhihu.dao.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wycm.zhihu.dao.mongodb.entity.TopicActivity;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by wycm on 2018/10/13.
 */
@Repository
public class ZhihuTopicActivityMongodbDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuTopicActivityRepository topicActivityRepository;

    public void replace(TopicActivity newTopicActivity){
        Query query = new Query();
        query.addCriteria(new Criteria("attached_info").is(newTopicActivity.getAttached_info()));
        TopicActivity zhihuTopicActivity = mongoTemplate.findOne(query, TopicActivity.class);
        if (zhihuTopicActivity == null){
            newTopicActivity.setCreateTime(new Date());
            newTopicActivity.setUpdateTime(new Date());
            topicActivityRepository.insert(newTopicActivity);
        } else {
            topicActivityRepository.delete(zhihuTopicActivity);
            Date createTime = zhihuTopicActivity.getCreateTime();
            newTopicActivity.setCreateTime(createTime);
            newTopicActivity.setUpdateTime(new Date());
            topicActivityRepository.insert(newTopicActivity);
        }
    }
    public void upsert(TopicActivity newTopicActivity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("attached_info").is(newTopicActivity.getAttached_info()));
        newTopicActivity.setUpdateTime(new Date());
        Update update = Update.fromDocument(Document.parse(JSON.toJSONString(newTopicActivity, SerializerFeature.UseISO8601DateFormat) ));
        mongoTemplate.upsert(query, update, "TopicActivity");
    }



}
