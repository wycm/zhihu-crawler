package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wycm on 2018/10/13.
 */
public interface ZhihuArticleRepository extends MongoRepository<Article, String> {
}
