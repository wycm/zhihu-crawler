package com.crawl.zhihu.task;

import com.crawl.util.Config;
import com.crawl.entity.Page;
import com.crawl.parser.zhihu.PageHandler;
import com.crawl.parser.zhihu.login.LoginPageHandler;
import com.crawl.parser.zhihu.tourist.TouristPageHandler;
import com.crawl.util.Constants;
import com.crawl.util.SimpleLogger;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 解析网页任务
 */
public class ParseTask implements Runnable {
    private static Logger logger = SimpleLogger.getSimpleLogger(ParseTask.class);
    private Page page;
    /**
     * 统计用户数量
     */
    public static AtomicInteger parseUserCount = new AtomicInteger(0);
    public static volatile boolean isStopDownload = false;
    private static PageHandler pageHandler;
    static {
        String crawlStrategy = Config.crawlStrategy;
        if(crawlStrategy.equals(Constants.LOGIN_PARSE_STRATEGY)){
            pageHandler = new LoginPageHandler();
        }
        else if (crawlStrategy.equals(Constants.TOURIST_PARSE_STRATEGY)){
            pageHandler = new TouristPageHandler();
        }
    }
    public ParseTask(Page page){
        this.page = page;
    }
    @Override
    public void run() {
        pageHandler.handle(page);
    }
}
