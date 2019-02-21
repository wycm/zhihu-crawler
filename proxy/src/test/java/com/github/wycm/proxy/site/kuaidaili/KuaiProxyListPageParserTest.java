package com.github.wycm.proxy.site.kuaidaili;

import com.github.wycm.common.util.SimpleHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by wycm on 2019-02-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:application.yaml"})
public class KuaiProxyListPageParserTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient(jedisPool);
        String r = simpleHttpClient.get("https://www.baidu.com");
        System.out.println(r);
    }
}