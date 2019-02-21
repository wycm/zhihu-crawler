package com.github.wycm.zhihu;

import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.AbstractHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.cookie.CookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;


/**
 * 用户抓取HttpClient
 */

@Slf4j
public class ZhihuHttpClient extends AbstractHttpClient {

    @Autowired
    private LocalIPService localIPService;

    public ZhihuHttpClient(JedisPool jedisPool){
        super();
        super.httpClient = new SimpleHttpClient(new SimpleHttpClient.IgnoreCookieStore(), 500, 2000, jedisPool);
    }

    @Override
    public LocalIPService getLocalIPService() {
        return localIPService;
    }
}
