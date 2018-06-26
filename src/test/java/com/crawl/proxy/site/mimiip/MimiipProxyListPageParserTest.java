package com.crawl.proxy.site.mimiip;


import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.SiteTestBase;
import com.crawl.zhihu.entity.Page;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

public class MimiipProxyListPageParserTest extends SiteTestBase {
    @Test
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.mimiip.com/gngao/");
        List<Proxy> proxyList = new MimiipProxyListPageParser().parse(page.getHtml());
        Assert.assertTrue(proxyList.size() > 0);
        for(Proxy p : proxyList){
            Assert.assertTrue(pattern.matcher(p.getIp()).matches());
            Assert.assertTrue(p.getPort() > 0 && p.getPort() <= 65535);
        }
    }
}
