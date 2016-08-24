package com.crawl.parser;

import com.crawl.entity.Page;
import com.crawl.entity.User;

/**
 * Created by yangwang on 16-8-23.
 */
public abstract class DetailPageParser implements Parser {
    public abstract User parse(Page page);
}
