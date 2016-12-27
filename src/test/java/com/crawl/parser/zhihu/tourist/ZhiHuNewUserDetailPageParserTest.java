package com.crawl.parser.zhihu.tourist;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.ZhiHuNewUserDetailPageParser;
import com.crawl.util.HttpClientUtil;
import org.junit.Test;

/**
 * Created by wy on 11/28/2016.
 */
public class ZhiHuNewUserDetailPageParserTest {
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
//        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
//        String url = "https://www.zhihu.com/people/excited-vczh/following";
        Page page = new Page();
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        page.setHtml(HttpClientUtil.getWebPage(url));
        page.setUrl(url);
        DetailPageParser parser = new ZhiHuNewUserTouristDetailPageParser();
        User user = parser.parse(page);
        System.out.println(user);
    }
}