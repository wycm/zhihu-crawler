
package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.zhihu.ZhiHuHttpClient;

public abstract class AbstractUserIndexDetailPageTest {
    public void testParse(String url, DetailPageParser parser){
        Page page = ZhiHuHttpClient.getInstance().getWebPage(url);
        User user = parser.parse(page);
        System.out.println(user);
    }
}