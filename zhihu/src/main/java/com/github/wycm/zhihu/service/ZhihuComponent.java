package com.github.wycm.zhihu.service;

import com.github.wycm.common.CommonProperties;
import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.proxy.ProxyHttpClient;
import com.github.wycm.proxy.ProxyPageProxyPool;
import com.github.wycm.zhihu.ZhihuHttpClient;
import com.github.wycm.zhihu.dao.mongodb.*;
import com.github.wycm.zhihu.parser.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
@Data
public class ZhihuComponent {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ProxyQueue proxyQueue;

    @Autowired
    private ProxyHttpClient proxyHttpClient;

    @Autowired
    private ZhihuHttpClient zhihuHttpClient;

    @Autowired
    private CrawledUrlMongodbDao crawledUrlMongodbDao;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private ZhihuUserMongodbDao zhihuUserMongodbDao;

    @Autowired
    private ZhihuUserRepository zhihuUserRepository;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private ProxyPageProxyPool proxyPageProxyPool;

    @Autowired
    private ZhihuUserPageParser zhihuUserPageParser;

    @Autowired
    private LocalIPService localIPService;

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private ZhihuTopicMongoPageParser zhihuTopicMongoPageParser;

    @Autowired
    private ZhihuTopicMongodbDao topicMongodbDao;

    @Autowired
    private ZhihuTopicActivityMongodbDao zhihuTopicActivityMongodbDao;

    @Autowired
    private ZhihuTopicActivityMongoPageParser zhihuTopicActivityMongoPageParser;

    @Autowired
    private ZhihuQuestionMongodbDao zhihuQuestionMongodbDao;

    @Autowired
    private ZhihuAnswerMongodbDao zhihuAnswerMongodbDao;

    @Autowired
    private ZhihuArticleMongodbDao zhihuArticleMongodbDao;
}
