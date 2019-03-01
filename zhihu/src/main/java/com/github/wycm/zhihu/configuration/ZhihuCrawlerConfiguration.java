package com.github.wycm.zhihu.configuration;

import com.github.wycm.proxy.ProxyHttpClient;
import com.github.wycm.zhihu.ZhihuHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;


@Configuration
@Slf4j
public class ZhihuCrawlerConfiguration {
    @Bean
    public ProxyHttpClient getProxyHttpClient(JedisPool jedisPool){
        return new ProxyHttpClient(jedisPool);
    }

    @Bean
    public ZhihuHttpClient getZhihuHttpClient(JedisPool jedisPool){
        return new ZhihuHttpClient(jedisPool);
    }

}
