package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by wycm on 2018/10/13.
 */
@Repository
public class ZhihuAnswerMongodbDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuAnswerRepository zhihuAnswerRepository;

    public void replace(Answer answer){
        Query query = new Query();
        query.addCriteria(new Criteria("url").is(answer.getUrl()));
        Answer oldAnswer = mongoTemplate.findOne(query, Answer.class);
        if (oldAnswer == null){
            answer.setCreateTime(new Date());
            answer.setUpdateTime(new Date());
            zhihuAnswerRepository.insert(answer);
        } else {
            zhihuAnswerRepository.delete(oldAnswer);
            Date createTime = oldAnswer.getCreateTime();
            answer.setCreateTime(createTime);
            answer.setUpdateTime(new Date());
            zhihuAnswerRepository.insert(answer);
        }
    }


}
