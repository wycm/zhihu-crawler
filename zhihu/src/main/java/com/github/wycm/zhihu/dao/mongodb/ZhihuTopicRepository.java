package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wycm on 2018/10/13.
 */
public interface ZhihuTopicRepository extends MongoRepository<Topic, String> {
}
