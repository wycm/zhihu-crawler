package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.junit.Test;

/**
 * Created by wy on 11/28/2016.
 */
public class ZhiHuNewUserIndexDetailPageParserTest extends AbstractUserIndexDetailPageTest{
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
        DetailPageParser parser = ZhiHuNewUserIndexDetailPageParser.getInstance();
        super.testParse(url, parser);
    }
}