package com.crawl.zhihu.parser;

import com.crawl.core.parser.DetailPageParser;
import org.junit.Test;

public class ZhiHuNewUserDetailPageParserTest extends AbstractUserIndexDetailPageTest{
    @Test
    public void testParse(){
        /**
         * 新版本页面
         */
//        String url = "https://www.zhihu.com/people/cheng-yi-nan/following";
//        String url = "https://www.zhihu.com/people/excited-vczh/following";
        String url = "https://www.zhihu.com/people/Vincen.t/following";
        DetailPageParser parser = new ZhiHuNewUserDetailPageParser();
        super.testParse(url, parser);
    }
}