package com.crawl.core.parser;

import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;

public interface DetailPageParser extends Parser {
    User parseDetailPage(Page page);
}
