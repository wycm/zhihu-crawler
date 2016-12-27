package com.crawl.parser.zhihu;

import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.login.ZhiHuNewUserLoginDetailPageParser;
import org.junit.Test;

/**
 * Created by wy on 11/28/2016.
 */
public class ZhiHuNewUserDetailPageParserTest extends AbstractUserIndexDetailPageTest{
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
//        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
//        String url = "https://www.zhihu.com/people/excited-vczh/following";
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        DetailPageParser parser = new ZhiHuNewUserLoginDetailPageParser();
        super.testParse(url, parser);
    }
}