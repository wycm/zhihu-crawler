package com.crawl.parser;

import com.crawl.entity.User;

/**
 * Created by yangwang on 16-8-23.
 */
public interface DetailPageParser extends PageType{
    public User parse();
}
