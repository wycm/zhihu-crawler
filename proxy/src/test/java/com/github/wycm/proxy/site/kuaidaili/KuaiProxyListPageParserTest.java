package com.github.wycm.proxy.site.kuaidaili;

import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.SimpleHttpClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by wycm on 2019-03-06.
 */
public class KuaiProxyListPageParserTest {

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient(null);
        String r = simpleHttpClient.get("https://www.kuaidaili.com/free/");
        KuaiProxyListPageParser pageParser = new KuaiProxyListPageParser();
        List<Proxy> proxyList =pageParser.parse(r);
        Assert.assertTrue(proxyList.size() > 1);
    }
}