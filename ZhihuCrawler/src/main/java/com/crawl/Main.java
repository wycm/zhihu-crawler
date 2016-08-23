package com.crawl;

import com.crawl.httpclient.HttpClient;
import com.crawl.zhihu.ZhiHuHttpClient;

/**
 * Created by Administrator on 2016/8/23 0023.
 * 爬虫入口
 */
public class Main {
    public static void main(String args []){
        HttpClient httpClient = ZhiHuHttpClient.getInstance();
    }
}
