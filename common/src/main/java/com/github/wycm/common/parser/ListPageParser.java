package com.github.wycm.common.parser;



import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Page;

import java.util.List;

public interface ListPageParser<T> extends Parser {
    default List<T> parseListPage(Page page){
        return null;
    }

    default List<T> parseListPage(Page page, CrawlerMessage crawlerMessage){
        return null;
    }
}
