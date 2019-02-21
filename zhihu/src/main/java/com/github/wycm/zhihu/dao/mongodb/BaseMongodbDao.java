package com.github.wycm.zhihu.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wycm on 2018/10/24.
 */
public abstract class BaseMongodbDao<T> {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<T> findEarliestUpdate(Class<T> tClass, Date updateTime, int limit){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("updateTime").lt(updateTime));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"updateTime")));
        query.limit(limit);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, tClass);
    }
}
