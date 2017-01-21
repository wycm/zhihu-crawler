package com.crawl.zhihu.task;


import com.crawl.core.parser.DetailPageParser;
import com.crawl.core.parser.ListPageParser;
import com.crawl.core.util.Config;
import com.crawl.core.util.SimpleInvocationHandler;
import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import com.crawl.zhihu.parser.ZhiHuNewUserDetailPageParser;
import com.crawl.zhihu.parser.ZhiHuUserListPageParser;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import static com.crawl.core.util.Constants.USER_FOLLOWEES_URL;
import static com.crawl.zhihu.ZhiHuHttpClient.parseUserCount;

public class DetailListPageTask extends AbstractPageTask{
    private static Logger logger = SimpleLogger.getSimpleLogger(DetailListPageTask.class);
    private static ListPageParser proxyUserListPageParser;
    static {
        proxyUserListPageParser = getProxyUserListPageParser();
    }


    public DetailListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }

    /**
     * 代理类
     * @return
     */
    private static ListPageParser getProxyUserListPageParser(){
        ListPageParser userListPageParser = ZhiHuUserListPageParser.getInstance();
        InvocationHandler invocationHandler = new SimpleInvocationHandler(userListPageParser);
        ListPageParser proxyUserListPageParser = (ListPageParser) Proxy.newProxyInstance(userListPageParser.getClass().getClassLoader(),
                userListPageParser.getClass().getInterfaces(), invocationHandler);
        return proxyUserListPageParser;
    }

    @Override
    void retry() {
        zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, Config.isProxy));
    }

    @Override
    void handle(Page page) {
        List<User> list = proxyUserListPageParser.parseListPage(page);
        for(User u : list){
            logger.info("解析用户成功:" + u.toString());
            if(Config.dbEnable){
                if (!zhiHuDao1.isExistUser(u.getUserToken())){
                    zhiHuDao1.insertUser(u);
                    parseUserCount.incrementAndGet();
                }
                for (int j = 0; j < u.getFollowees() / 20; j++){
                    String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
                }
            }
            else if(!Config.dbEnable || zhiHuHttpClient.getDetailListPageThreadPool().getActiveCount() == 1){
                parseUserCount.incrementAndGet();
                for (int j = 0; j < u.getFollowees(); j++){
                    String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
                }
            }
        }
    }
}
