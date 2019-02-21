package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.CrawledUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wycm on 2018/10/13.
 */
public interface CrawledUrlRepository  extends MongoRepository<CrawledUrl, String> {
}
