package com.crawl.proxy.site.ip181;


import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.zhihu.entity.Page;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class Ip181ProxyListPageParserTest {
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html");
        List<Proxy> proxyList = new Ip181ProxyListPageParser().parse(page.getHtml());
        Assert.assertTrue(proxyList.size() > 0);
        for(Proxy p : proxyList){
            Assert.assertNotNull(p.getIp());
            Assert.assertNotNull(p.getPort());
        }
    }
}
