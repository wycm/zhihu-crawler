package com.crawl.parser.zhihu.tourist;

import com.crawl.parser.zhihu.ZhiHuNewUserDetailPageParser;
import com.crawl.util.Config;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.PageHandler;
import com.crawl.parser.zhihu.login.LoginPageHandler;
import com.crawl.util.Constants;
import com.crawl.util.Md5Util;
import com.crawl.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.task.DownloadTask;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.crawl.zhihu.task.ParseTask.isStopDownload;
import static com.crawl.zhihu.task.ParseTask.parseUserCount;

/**
 * 游客模式页面处理器
 */
public class TouristPageHandler implements PageHandler{
    private static Logger logger = SimpleLogger.getSimpleLogger(LoginPageHandler.class);
    private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();

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
        parser = new ZhiHuNewUserDetailPageParser();
        User u = parser.parse(page);
        logger.info("解析用户成功:" + u.toString());
        if(Config.dbEnable){
            ZhiHuDAO.insertToDB(u);
        }
        parseUserCount.incrementAndGet();
        for(int i = 0;i < u.getFollowees()/20 + 1;i++) {
            /**
             * 当下载网页队列小于100时才获取该用户关注用户
             * 防止下载网页线程池任务队列过量增长
             */
            if (!isStopDownload && zhiHuHttpClient.getDownloadThreadExecutor().getQueue().size() <= 100) {
                /**
                 * 获取关注用户列表,因为知乎每次最多返回20个关注用户
                 */
                String userFolloweesUrl = formatUserFolloweesUrl(20*i);
                handleUrl(userFolloweesUrl);
            }
        }
    }
    public String formatUserFolloweesUrl(int offset){
        String url = "https://www.zhihu.com/api/v4/members/cheng-yi-nan/followees?include=data%5B*%5D.answer_count%2Carticles_count%2Cfollower_count%2C" +
                "is_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=" + offset + "&limit=20";
        return url;
    }
    private void handleUrl(String url){
        HttpGet request = new HttpGet(url);
        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
        if(!Config.dbEnable){
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(request));
            return ;
        }
        String md5Url = Md5Util.Convert2Md5(url);
        boolean isRepeat = ZhiHuDAO.insertUrl(md5Url);
        if(!isRepeat ||
                (!zhiHuHttpClient.getDownloadThreadExecutor().isShutdown() &&
                        zhiHuHttpClient.getDownloadThreadExecutor().getQueue().size() < 30)){
            /**
             * 防止互相等待，导致死锁
             */
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(request));
        }
    }
    private void handleTouristListPage(Page page){
        /**
         * "我关注的人"列表页
         */
        if(!isStopDownload && zhiHuHttpClient.getDownloadThreadExecutor().getQueue().size() <= 100){
            List<String> urlTokenList = JsonPath.parse(page.getHtml()).read("$.data..url_token");
            for (String s : urlTokenList){
                handleUrl(Constants.INDEX_URL + "/people/" + s + "/following");
            }
        }
    }
}
