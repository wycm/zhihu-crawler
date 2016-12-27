package com.crawl.zhihu.task;

import com.crawl.config.Config;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.PageHandler;
import com.crawl.parser.zhihu.ZhiHuNewUserDetailPageParser;
import com.crawl.parser.zhihu.ZhiHuUserFollowingListPageParser;
import com.crawl.parser.zhihu.ZhiHuUserIndexDetailPageParser;
import com.crawl.parser.zhihu.login.LoginPageHandler;
import com.crawl.parser.zhihu.login.ZhiHuNewUserLoginDetailPageParser;
import com.crawl.parser.zhihu.tourist.TouristPageHandler;
import com.crawl.parser.zhihu.tourist.ZhiHuNewUserTouristDetailPageParser;
import com.crawl.util.Constants;
import com.crawl.util.Md5Util;
import com.crawl.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/8/24 0024.
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
