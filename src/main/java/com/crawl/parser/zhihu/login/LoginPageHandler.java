package com.crawl.parser.zhihu.login;

import com.crawl.config.Config;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.parser.zhihu.PageHandler;
import com.crawl.parser.zhihu.ZhiHuUserFollowingListPageParser;
import com.crawl.parser.zhihu.ZhiHuUserIndexDetailPageParser;
import com.crawl.util.Constants;
import com.crawl.util.Md5Util;
import com.crawl.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.task.DownloadTask;
import com.crawl.zhihu.task.ParseTask;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.crawl.zhihu.task.ParseTask.isStopDownload;
import static com.crawl.zhihu.task.ParseTask.parseUserCount;

/**
 * Created by yang.wang on 12/27/16.
 * 登录模式页面处理器
 */
public class LoginPageHandler implements PageHandler{
    private static Logger logger = SimpleLogger.getSimpleLogger(LoginPageHandler.class);
    private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
    private void handleLoginListPage(Page page) {
        /**
         * "我关注的人"列表页
         */
        if(!isStopDownload && zhiHuHttpClient.getDownloadThreadExecutor().getQueue().size() <= 100){
            List<String> userIndexHref = ZhiHuUserFollowingListPageParser.getInstance().parse(page);
            for(String url : userIndexHref){
                handleUrl(url);
            }
        }
    }
    /**
     * 登录模式详情页解析
     */
    private void handleLoginDetailPage(Page page, Document doc){
        DetailPageParser parser = null;
        if (doc.select("div[id=ProfileHeader]").size() > 0){
            //新版主页
            parser = new ZhiHuNewUserLoginDetailPageParser();
        }
        else {
            //旧版主页
            parser = ZhiHuUserIndexDetailPageParser.getInstance();
        }
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
                String userFolloweesUrl = formatUserFolloweesUrl(20*i, u.getHashId());
                handleUrl(userFolloweesUrl);
            }
        }
    }
    public String formatUserFolloweesUrl(int offset, String userHashId){
        String url = "https://www.zhihu.com/node/ProfileFolloweesListV2?params={%22offset%22:" + offset + ",%22order_by%22:%22created%22,%22hash_id%22:%22" + userHashId + "%22}";
        url = url.replaceAll("[{]", "%7B").replaceAll("[}]", "%7D").replaceAll(" ", "%20");
        return url;
    }
    private void handleUrl(String url){
        if(!Config.dbEnable){
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url));
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
            zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url));
        }
    }
    @Override
    public void handle(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        if(doc.select("title").size() != 0) {
            /**
             * 详情页
             */
            handleLoginDetailPage(page, doc);
        }
        else {
            handleLoginListPage(page);
        }
    }
}
