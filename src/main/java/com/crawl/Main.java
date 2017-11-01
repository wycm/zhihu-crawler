package com.crawl;

import com.crawl.proxy.ProxyHttpClient;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫入口
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String args []){
        ProxyHttpClient.getInstance().startCrawl();
        ZhiHuHttpClient.getInstance().startCrawl();
    }
}
