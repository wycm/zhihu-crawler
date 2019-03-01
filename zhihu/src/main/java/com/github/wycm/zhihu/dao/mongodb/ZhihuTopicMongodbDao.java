package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wycm on 2018/10/13.
 */
@Repository
public class ZhihuTopicMongodbDao extends BaseMongodbDao<Topic>{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuTopicRepository zhihuTopicRepository;

    public void replace(Topic topic){
        Query query = new Query();
        query.addCriteria(new Criteria("url").is(topic.getUrl()));
        Topic oldTopic = mongoTemplate.findOne(query, Topic.class);
        if (oldTopic == null){
            Date date = new Date();
            topic.setCreateTime(date);
            topic.setUpdateTime(date);
            topic.setTopicActivityUpdateTime(date);
            zhihuTopicRepository.insert(topic);
        } else {
            zhihuTopicRepository.delete(oldTopic);
            Date createTime = oldTopic.getCreateTime();
            topic.setCreateTime(createTime);
            topic.setUpdateTime(new Date());
            zhihuTopicRepository.insert(topic);
        }
    }

    /**
     * 更新topic动态最后更新时间
     */
    public void updateTopicActivityUpdateTime(String id){
        Query query = new Query(new Criteria("_id").is(id));
        Update update = new Update();
        update.set("topicActivityUpdateTime", new Date());
        mongoTemplate.updateFirst(query, update, Topic.class);
    }

    public List<Topic> findEarliestTopic(Date updateTime, int limit){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("topicActivityUpdateTime").is(null), Criteria.where("topicActivityUpdateTime").lt(updateTime));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"topicActivityUpdateTime")));
        query.limit(limit);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Topic.class);
    }
}
