package com.github.wycm.proxy.site.qydaili;

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
public class QydailiProxyListPageParserTest {

    @Test
    public void parse() throws ExecutionException, InterruptedException {
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient(null);
        String r = simpleHttpClient.get("http://www.qydaili.com/free/?action=china&page=1");
        QydailiProxyListPageParser pageParser = new QydailiProxyListPageParser();
        List<Proxy> proxyList =pageParser.parse(r);
        Assert.assertTrue(proxyList.size() > 1);
    }
}