package com.crawl.proxy.site.xicidaili;


import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.SiteTestBase;
import com.crawl.zhihu.entity.Page;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class XicidailiProxyListPageParserTest extends SiteTestBase {
    @Test
    public void testParse() throws IOException {
        RequestConfig.Builder requestBuilder = HttpClientUtil.getRequestConfigBuilder();
        HttpGet request = new HttpGet("http://www.xicidaili.com/wt/1.html");
        request.setConfig(requestBuilder.build());
        Page page = ProxyHttpClient.getInstance().getWebPage(request);
        List<Proxy> proxyList = new XicidailiProxyListPageParser().parse(page.getHtml());
        Assert.assertTrue(proxyList.size() > 0);
        for(Proxy p : proxyList){
            Assert.assertTrue(pattern.matcher(p.getIp()).matches());
            Assert.assertTrue(p.getPort() > 0 && p.getPort() <= 65535);
        }
    }
}
