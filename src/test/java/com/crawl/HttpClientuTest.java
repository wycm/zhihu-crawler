package com.crawl;

import com.crawl.core.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class HttpClientuTest {
    private final static int j = 0;
    public static void main(String[] args) throws IOException {
        HttpGet request = new HttpGet("https://www.baidu.com");
        request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(new HttpHost("localhost")).build());
        HttpClientUtil.getWebPage(request);
    }
}
