package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.CrawledUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by wycm on 2018/10/13.
 */
@Repository
public class CrawledUrlMongodbDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CrawledUrlRepository crawledUrlRepository;

    public boolean insertIfNotExist(CrawledUrl crawledUrl){

        if (!exist(crawledUrl)){
            crawledUrlRepository.insert(crawledUrl);
            return true;
        } else {
            return false;
        }
    }

    public boolean exist(CrawledUrl crawledUrl){
        Query query = new Query();
        query.addCriteria(new Criteria("url").is(crawledUrl.getUrl()));
        CrawledUrl existCrawledUrl = mongoTemplate.findOne(query, CrawledUrl.class);
        if (existCrawledUrl == null){
            return false;
        } else {
            return true;
        }
    }

    public boolean exist(String url){
        Query query = new Query();
        query.addCriteria(new Criteria("url").is(url));
        CrawledUrl existCrawledUrl = mongoTemplate.findOne(query, CrawledUrl.class);
        if (existCrawledUrl == null){
            return false;
        } else {
            return true;
        }
    }

}
