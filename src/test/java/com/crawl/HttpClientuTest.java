package com.crawl;

import com.crawl.util.HttpClientUtil;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * Created by yang.wang on 12/26/16.
 */
public class HttpClientuTest {
    private final static int j = 0;
    public static void main(String[] args){
        HttpClientUtil.getWebPage("https://www.baidu.com");
        HttpClientUtil.getWebPage("https://www.google.com");
    }
}
