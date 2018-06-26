package com.crawl.zhihu.parser;

import com.crawl.core.parser.DetailPageParser;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ZhiHuNewUserDetailPageParserTest{
    @Test
    public void testParse() throws IOException {
        /**
         * 新版本页面
         */
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        DetailPageParser parser = ZhiHuNewUserDetailPageParser.getInstance();
        Page page = ZhiHuHttpClient.getInstance().getWebPage(url);
        User user = parser.parseDetailPage(page);
        Assert.assertNotNull(user.getUsername());
    }
}