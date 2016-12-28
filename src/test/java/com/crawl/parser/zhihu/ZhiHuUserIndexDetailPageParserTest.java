package com.crawl.parser.zhihu;

import com.crawl.parser.DetailPageParser;
import org.junit.Test;


public class ZhiHuUserIndexDetailPageParserTest extends AbstractUserIndexDetailPageTest{
    @Test
    public void testParse(){
        /**
         * 老版本页面
         */
        String url = "https://www.zhihu.com/people/liaoxuefeng/followees";
        DetailPageParser parser = ZhiHuUserIndexDetailPageParser.getInstance();
        super.testParse(url, parser);
    }
}