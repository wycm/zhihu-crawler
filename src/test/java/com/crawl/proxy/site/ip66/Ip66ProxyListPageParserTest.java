package com.crawl.proxy.site.ip66;


import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.SiteTestBase;
import com.crawl.zhihu.entity.Page;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class Ip66ProxyListPageParserTest extends SiteTestBase {
    @Test
    public void testParse() throws IOException {
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.66ip.cn/index.html");
        page.setHtml(new String(page.getHtml().getBytes("GB2312"), "GB2312"));
        List<Proxy> proxyList = new Ip66ProxyListPageParser().parse(page.getHtml());
        Assert.assertTrue(proxyList.size() > 0);
        for(Proxy p : proxyList){
            Assert.assertTrue(pattern.matcher(p.getIp()).matches());
            Assert.assertTrue(p.getPort() > 0 && p.getPort() <= 65535);
        }
    }
}
