package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Question;
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
public class ZhihuQuestionMongodbDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuQuestionRepository zhihuQuestionRepository;

    public void insert(Question question){
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(question.getId()));
        Question oldQuestion = mongoTemplate.findOne(query, Question.class);
        if (oldQuestion == null){
            question.setCreateTime(new Date());
            question.setUpdateTime(new Date());
            zhihuQuestionRepository.insert(question);
        }
    }

    public Question findQuestionByUrl(String url){
        Query query = new Query(new Criteria("url").is(url));
        return mongoTemplate.findOne(query, Question.class);
    }

    public Question findQuestionById(String id){
        Query query = new Query(new Criteria("_id").is(id));
        return mongoTemplate.findOne(query, Question.class);
    }

    /**
     * 更新问题答案最后更新时间
     */
    public void updateQuestionAnswerUpdateTime(String id){
        Query query = new Query(new Criteria("_id").is(id));
        Update update = new Update();
        update.set("answerUpdateTime", new Date());
        mongoTemplate.updateFirst(query, update, Question.class);
    }

    public void updateQuestionDetail(String id, String detail){
        Query query = new Query(new Criteria("_id").is(id));
        Update update = new Update();
        update.set("updateTime", new Date());
        update.set("detail", detail);
        mongoTemplate.updateFirst(query, update, Question.class);
    }

    public List<Question> findEarliestQuestion(Date updateTime, int limit) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("answerUpdateTime").is(null), Criteria.where("answerUpdateTime").lt(updateTime));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"answerUpdateTime")));
        query.limit(limit);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Question.class);
    }

    public List<Question> findQuestionDetailIsNull(int limit) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("detail").is(null));
        query.limit(limit);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Question.class);
    }


}
