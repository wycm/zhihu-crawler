
package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.zhihu.ZhiHuHttpClient;

/**
 * Created by wy on 11/28/2016.
 */
public abstract class AbstractUserIndexDetailPageTest {
    public void testParse(String url, DetailPageParser parser){
//        ZhiHuHttpClient.getInstance().getHttpClientContext().getCookieStore().clear();
        Page page = ZhiHuHttpClient.getInstance().getWebPage(url);
        User user = parser.parse(page);
        System.out.println(user);
    }
}