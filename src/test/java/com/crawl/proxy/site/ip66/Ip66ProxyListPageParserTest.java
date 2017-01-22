package com.crawl.proxy.site.ip66;


import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.xicidaili.XicidailiProxyListPageParser;
import com.crawl.zhihu.entity.Page;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class Ip66ProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.66ip.cn/index.html");
        page.setHtml(new String(page.getHtml().getBytes("GB2312"), "GB2312"));
        List<Proxy> urlList = new Ip66ProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
