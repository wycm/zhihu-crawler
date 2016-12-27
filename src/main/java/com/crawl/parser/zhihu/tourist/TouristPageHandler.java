package com.crawl.parser.zhihu.tourist;

import com.crawl.config.Config;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.PageHandler;
import com.crawl.parser.zhihu.login.LoginPageHandler;
import com.crawl.util.SimpleLogger;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by yang.wang on 12/27/16.
 * 游客模式页面处理器
 */
public class TouristPageHandler implements PageHandler{
    private static Logger logger = SimpleLogger.getSimpleLogger(LoginPageHandler.class);
    @Override
    public void handle(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        if(doc.select("title").size() != 0) {
            /**
             * 详情页
             */
            handleTouristDetailPage(page, doc);
        }
        else {
            handleTouristListPage(page);
        }
    }
    /**
     * 游客模式详情页解析
     */
    private void handleTouristDetailPage(Page page, Document doc){
        DetailPageParser parser = null;
        parser = new ZhiHuNewUserTouristDetailPageParser();
        User u = parser.parse(page);
        logger.info("解析用户成功:" + u.toString());
        if(Config.dbEnable){
            ZhiHuDAO.insertToDB(u);
        }
    }
    private void handleTouristListPage(Page page){

    }
}
