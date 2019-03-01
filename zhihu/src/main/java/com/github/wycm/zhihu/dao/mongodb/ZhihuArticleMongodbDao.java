package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Article;
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
public class ZhihuArticleMongodbDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ZhihuArticleRepository zhihuArticleRepository;

    public void replace(Article article){
        Query query = new Query();
        query.addCriteria(new Criteria("url").is(article.getUrl()));
        Article oldArticle = mongoTemplate.findOne(query, Article.class);
        if (oldArticle == null){
            article.setCreateTime(new Date());
            article.setUpdateTime(new Date());
            zhihuArticleRepository.insert(article);
        } else {
            zhihuArticleRepository.delete(oldArticle);
            Date createTime = oldArticle.getCreateTime();
            article.setCreateTime(createTime);
            article.setUpdateTime(new Date());
            zhihuArticleRepository.insert(article);
        }
    }


}
