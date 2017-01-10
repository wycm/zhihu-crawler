package com.crawl.proxy.site.mimiip;


import com.crawl.proxy.ProxyHttpClient;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.ip181.Ip181ProxyListPageParser;
import com.crawl.zhihu.entity.Page;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class MimiipProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.mimiip.com/gngao/");
        List<Proxy> urlList = new MimiipProxyListPageParser().parse(page.getHtml());
        System.out.println(urlList.size());
    }
}
