package com.crawl;

import com.crawl.core.util.HttpClientUtil;

import java.io.IOException;

public class HttpClientuTest {
    private final static int j = 0;
    public static void main(String[] args) throws IOException {
        HttpClientUtil.getWebPage("https://www.baidu.com");
        HttpClientUtil.getWebPage("https://www.google.com");
    }
}
