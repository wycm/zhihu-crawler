package com.github.wycm.proxy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by wycm on 2018/10/14.
 */
@Configuration
@Slf4j
public class JedisConfiguration {
    @Value("${spring.jedis.host}")
    private String host;

    @Value("${spring.jedis.port}")
    private String port;

    @Autowired
    private Environment env;

    @Bean
    public JedisPool getJedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(200);
        jedisPoolConfig.setMaxIdle(200);
        jedisPoolConfig.setMinIdle(0);
        JedisPool pool = null;
        String password = env.getProperty("spring.jedis.password");
        if (password  != null){
            pool = new JedisPool(jedisPoolConfig, host, 6379, 3000, password);
        } else {
            pool = new JedisPool(jedisPoolConfig, host);
        }
        return pool;
    }
}
