package com.github.wycm.zhihu.dao.mongodb;

import com.github.wycm.zhihu.dao.mongodb.entity.ZhihuUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wycm on 2018/10/13.
 */
@Repository
public interface ZhihuUserRepository extends MongoRepository<ZhihuUser, String> {
}
