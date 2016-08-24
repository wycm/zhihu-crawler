package com.crawl.parser;

import com.crawl.entity.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwang on 16-8-23.
 */
public abstract class ListPageParser implements Parser {
    public abstract List<String> parse(Page page);
}
