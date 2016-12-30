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
    private static Logger logger = SimpleLogger.getLogger(Main.class);
    public static void main(String args []){
        String startURL = Config.startURL;
        ProxyHttpClient.getInstance().startCrawl(
                "http://www.xicidaili.com/wt",
                "http://www.xicidaili.com/nn",
                "http://www.xicidaili.com/wn");
        ZhiHuHttpClient.getInstance().startCrawl(startURL);
    }
}
