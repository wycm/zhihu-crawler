package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.ZhihuUser;
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
public class ZhihuUserMongodbDao extends BaseMongodbDao<ZhihuUser>{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuUserRepository zhihuUserRepository;

    public void replace(ZhihuUser zhihuUser){
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(zhihuUser.getId()));
        ZhihuUser oldZhihuUser = mongoTemplate.findOne(query, ZhihuUser.class);
        if (oldZhihuUser == null){
            Date date = new Date();
            zhihuUser.setCreateTime(date);
            zhihuUser.setUpdateTime(date);
            zhihuUserRepository.insert(zhihuUser);
        } else {
            Update update = new Update();
            update.set("updateTime", new Date());
            update.set("name", zhihuUser.getName());
            mongoTemplate.updateFirst(query, update, ZhihuUser.class);
        }
    }
    public List<ZhihuUser> findEarliestZhihuUser(String timeAttrName, Date updateTime, int limit) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where(timeAttrName).is(null), Criteria.where(timeAttrName).lt(updateTime));
        query.with(new Sort(Sort.Direction.ASC,timeAttrName));
        query.limit(limit);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, ZhihuUser.class);
    }
}
