package com.crawl.parser.zhihu.tourist;

import com.crawl.parser.zhihu.ZhiHuNewUserDetailPageParser;
import com.crawl.util.Constants;

/**
 * Created by yang.wang on 12/27/16.
 */
public class ZhiHuNewUserTouristDetailPageParser extends ZhiHuNewUserDetailPageParser{
    @Override
    public String getParseStrategy() {
        return Constants.TOURIST_PARSE_STRATEGY;
    }
}
