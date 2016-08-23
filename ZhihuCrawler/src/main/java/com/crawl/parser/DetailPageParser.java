package com.crawl.parser;

import com.crawl.entity.Page;
import com.crawl.entity.User;

/**
 * Created by yangwang on 16-8-23.
 */
public class DetailPageParser implements Parser {
    public User parse(Page page){
        User user = new User();
        return user;
    }
}
