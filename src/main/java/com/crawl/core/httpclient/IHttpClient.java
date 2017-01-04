package com.crawl.core.httpclient;

/**
 * <p>@description:爬虫客户端定义接口 </p>
 *
 * @projectName: ZhiHuSpider
 * @className: ISpiderHttpClient
 * @author: yangshuang
 * @date: 2016/12/17 11:07
 */
public interface IHttpClient {


    /**
     * 初始化客户端
     * 模拟登录，持久化Cookie到本地
     * 不用以后每次都登录
     */
    abstract void initHttpClient();

    /**
     * 爬虫入口
     *
     * @param url
     */
    void startCrawl(String url);
}
