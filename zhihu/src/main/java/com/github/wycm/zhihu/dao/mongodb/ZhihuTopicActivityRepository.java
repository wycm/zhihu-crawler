package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.TopicActivity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ZhihuTopicActivityRepository extends MongoRepository<TopicActivity, String> {
}
