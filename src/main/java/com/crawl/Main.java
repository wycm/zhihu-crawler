package com.crawl;

import com.crawl.config.Config;
import com.crawl.util.SimpleLogger;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2016/8/23 0023.
 * 爬虫入口
 */
public class Main {
    private static Logger logger = SimpleLogger.getLogger(Main.class);
    public static void main(String args []){
        String startURL = Config.startURL;
        ZhiHuHttpClient.getInstance().startCrawl(startURL);
    }
}
