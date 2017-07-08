package com.crawl;

import com.crawl.core.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.StringReader;

public class HttpClientuTest {
    private final static int j = 0;
    public static void main(String[] args) throws IOException {
        String[] urlArray = new String[]{
                "https://www.zhihu.com/api/v4/members/yu-xing-yu-31/questions?include=data[*].created,answer_count,follower_count,author&offset=0&limit=20",
                "https://www.zhihu.com/api/v4/members/yaba/questions?include=data[*].created,answer_count,follower_count,author&offset=0&limit=20",
                "https://www.zhihu.com/api/v4/members/xiaodiyi/questions?include=data[*].created,answer_count,follower_count,author&offset=0&limit=20"
        };

        for (int i = 0; i < urlArray.length; i++) {
            String url = urlArray[i];
            HttpGet request = new HttpGet(url);
            request.setHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20");
//            request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(new HttpHost("111.68.124.10", 8080)).build());
            String content = HttpClientUtil.getWebPage(request);
            StringReader sr = new StringReader(content);
            System.out.println(sr);
        }
    }
}
