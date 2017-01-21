
package com.crawl.zhihu.parser;

import com.crawl.core.parser.DetailPageParser;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import com.crawl.zhihu.ZhiHuHttpClient;

import java.io.IOException;

public abstract class AbstractUserIndexDetailPageTest {
    public void testParse(String url, DetailPageParser parser) throws IOException {
        Page page = ZhiHuHttpClient.getInstance().getWebPage(url);
        User user = parser.parseDetailPage(page);
        System.out.println(user);
    }
}