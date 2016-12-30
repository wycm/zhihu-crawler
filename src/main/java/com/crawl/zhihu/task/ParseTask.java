package com.crawl.zhihu.task;

import com.crawl.core.util.Config;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.parser.PageHandler;
import com.crawl.zhihu.parser.login.LoginPageHandler;
import com.crawl.zhihu.parser.tourist.TouristPageHandler;
import com.crawl.core.util.Constants;
import com.crawl.core.util.SimpleLogger;
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
        boolean loginFlag = Config.isLogin;
        if(loginFlag){
            pageHandler = new LoginPageHandler();
        }
        else{
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
