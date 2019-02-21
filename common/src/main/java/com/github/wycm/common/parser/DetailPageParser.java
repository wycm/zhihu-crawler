package com.github.wycm.common.parser;


import com.github.wycm.common.Page;

public interface DetailPageParser<T> extends Parser {
    T parseDetailPage(Page page);
}
