package com.github.wycm.proxy;

import com.github.wycm.common.LocalIPService;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.cookie.CookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;


@Slf4j
public class ProxyHttpClient extends AbstractHttpClient {
    @Autowired
    private LocalIPService localIPService;

    private CookieStore initCookies(){
        return CrawlerUtils.parseCookies("yd_cookie=38f1de1d-c6ef-4787772385a8345b15307ea2c4afc8bf302a; Hm_lvt_1761fabf3c988e7f04bec51acd4073f4=1546613119,1547095208; Hm_lpvt_1761fabf3c988e7f04bec51acd4073f4=1547112489; _ydclearance=397000b35fa5214c9f2f8419-91c8-4b54-a1b8-34479c2c951d-1547181034", "http://www.66ip.cn/");
    }
    public ProxyHttpClient(JedisPool jedisPool){
        CookieStore cookieStore = initCookies();
        super.httpClient = new SimpleHttpClient(cookieStore, 500, 2000, jedisPool);
    }

    @Override
    public LocalIPService getLocalIPService() {
        return localIPService;
    }
}