package com.crawl.zhihu.task;


import com.crawl.core.parser.DetailPageParser;
import com.crawl.core.util.Config;
import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.dao.ZhiHuDAO;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import com.crawl.zhihu.parser.ZhiHuNewUserDetailPageParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import static com.crawl.zhihu.ZhiHuHttpClient.parseUserCount;


public class DetailPageTask extends AbstractPageTask {
    private static Logger logger = SimpleLogger.getSimpleLogger(DetailPageTask.class);

    public DetailPageTask(String url, boolean proxyFlag) {
        super(url, proxyFlag);
    }

    @Override
    void retry() {
        zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));
    }

    @Override
    void handle(Page page) {
        DetailPageParser parser = null;
        parser = ZhiHuNewUserDetailPageParser.getInstance();
        User u = parser.parse(page);
        logger.info("解析用户成功:" + u.toString());
        if(Config.dbEnable){
            ZhiHuDAO.insertUser(u);
        }
        parseUserCount.incrementAndGet();
        for(int i = 0;i < u.getFollowees() / 20 + 1;i++) {
            /**
             * 当下载网页队列小于100时才获取该用户关注用户
             * 防止下载网页线程池任务队列过量增长
             */
//            if (!isStopDownload && zhiHuHttpClient.getDetailPageThreadPool().getQueue().size() <= 500) {
                /**
                 * 获取关注用户列表,知乎每次最多返回20个关注用户
                 */
                String userFolloweesUrl = formatUserFolloweesUrl(u.getUserToken(), 20 * i);
                handleUrl(userFolloweesUrl);
//            }
        }
    }
    public String formatUserFolloweesUrl(String userToken, int offset){
        String url = "https://www.zhihu.com/api/v4/members/" + userToken + "/followees?include=data%5B*%5D.answer_count%2Carticles_count%2Cfollower_count%2C" +
                "is_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=" + offset + "&limit=20";
        return url;
    }
    private void handleUrl(String url){
        HttpGet request = new HttpGet(url);
        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
        if(!Config.dbEnable){
            zhiHuHttpClient.getListPageThreadPool().execute(new ListPageTask(request, Config.isProxy));
            return ;
        }
        if(zhiHuHttpClient.getListPageThreadPool().getQueue().size() < 100){
            zhiHuHttpClient.getListPageThreadPool().execute(new ListPageTask(request, Config.isProxy));
        }
    }
}
