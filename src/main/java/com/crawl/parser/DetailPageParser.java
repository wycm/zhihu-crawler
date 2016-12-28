package com.crawl.parser;

import com.crawl.entity.Page;
import com.crawl.entity.User;

public abstract class DetailPageParser implements Parser {
    public abstract User parse(Page page);
}
