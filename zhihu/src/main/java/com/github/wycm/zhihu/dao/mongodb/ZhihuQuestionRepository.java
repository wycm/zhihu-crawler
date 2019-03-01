package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wycm on 2018/10/13.
 */
public interface ZhihuQuestionRepository extends MongoRepository<Question, String> {
}
