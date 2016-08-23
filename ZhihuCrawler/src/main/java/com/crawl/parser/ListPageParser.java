package com.crawl.parser;

import com.crawl.entity.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwang on 16-8-23.
 */
public class ListPageParser implements Parser {
    public List<String> parse(Page page){
        List<String> detailUrlList = new ArrayList<String>();
        return detailUrlList;
    }
}
