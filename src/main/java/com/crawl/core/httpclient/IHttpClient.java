package com.crawl.core.httpclient;


public interface IHttpClient {


    /**
     * 初始化客户端
     * 模拟登录，持久化Cookie到本地
     * 不用以后每次都登录
     */
    void initHttpClient();
    /**
     * 爬虫入口
     */
    void startCrawl();
}
