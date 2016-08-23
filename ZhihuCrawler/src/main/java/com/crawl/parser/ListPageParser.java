package com.crawl.parser;

import com.crawl.entity.User;

import java.util.List;

/**
 * Created by yangwang on 16-8-23.
 */
public interface ListPageParser extends PageType{
    public List<String> parse();
}
