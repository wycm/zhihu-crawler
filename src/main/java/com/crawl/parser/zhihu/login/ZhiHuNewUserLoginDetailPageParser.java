package com.crawl.parser.zhihu.login;

import com.crawl.parser.zhihu.ZhiHuNewUserDetailPageParser;
import com.crawl.util.Constants;

/**
 * Created by yang.wang on 12/27/16.
 */
public class ZhiHuNewUserLoginDetailPageParser extends ZhiHuNewUserDetailPageParser{
    @Override
    public String getParseStrategy() {
        return Constants.LOGIN_PARSE_STRATEGY;
    }
}
