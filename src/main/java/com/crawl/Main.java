package com.crawl;

import com.crawl.core.util.Config;
import com.crawl.core.util.SimpleLogger;
import com.crawl.proxy.ProxyHttpClient;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.apache.log4j.Logger;

/**
 * 爬虫入口
 */
public class Main {
    private static Logger logger = SimpleLogger.getSimpleLogger(Main.class);
    public static void main(String args []){
        ProxyHttpClient.getInstance().startCrawl();
        ZhiHuHttpClient.getInstance().startCrawl();
    }
}
