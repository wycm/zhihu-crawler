package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wycm on 2018/10/13.
 */
public interface ZhihuAnswerRepository extends MongoRepository<Answer, String> {
}
