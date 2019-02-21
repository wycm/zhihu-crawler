package com.github.wycm.proxy.site.kuaidaili;

import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.zhihu.SimpleSpringJUnit4ClassRunner;
import com.github.wycm.zhihu.ZhihuCrawlerApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by wycm on 2019-02-17.
 */
@RunWith(SimpleSpringJUnit4ClassRunner.class)
@SpringBootTest(classes= ZhihuCrawlerApplication.class)
public class KuaiProxyListPageParserTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient(jedisPool);
        String r = simpleHttpClient.get("https://www.kuaidaili.com/free/");
        KuaiProxyListPageParser pageParser = new KuaiProxyListPageParser();
        List<Proxy> proxyList =pageParser.parse(r);
        Assert.assertTrue(proxyList.size() > 1);
    }
}