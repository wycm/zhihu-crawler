package com.crawl.zhihu.task;


import com.crawl.core.dao.ConnectionManager;
import com.crawl.core.parser.DetailPageParser;
import com.crawl.core.parser.ListPageParser;
import com.crawl.core.util.Config;
import com.crawl.core.util.Md5Util;
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
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import static com.crawl.core.util.Constants.USER_FOLLOWEES_URL;
import static com.crawl.zhihu.ZhiHuHttpClient.parseUserCount;

/**
 * 知乎用户列表详情页task
 */
public class DetailListPageTask extends AbstractPageTask{
    private static Logger logger = SimpleLogger.getSimpleLogger(DetailListPageTask.class);
    private static ListPageParser proxyUserListPageParser;
    /**
     * Thread-数据库连接
     */
    private static Map<Thread, Connection> connectionMap = new ConcurrentHashMap<>();
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
        if(!page.getHtml().startsWith("{\"paging\"")){
            //代理异常，未能正确返回目标请求数据，丢弃
            currentProxy = null;
            return;
        }
        List<User> list = proxyUserListPageParser.parseListPage(page);
        for(User u : list){
            logger.info("解析用户成功:" + u.toString());
            if(Config.dbEnable){
                Connection cn = getConnection();
                if (zhiHuDao1.insertUser(cn, u)){
                    parseUserCount.incrementAndGet();
                }
                for (int j = 0; j < u.getFollowees() / 20; j++){
                    if (zhiHuHttpClient.getDetailListPageThreadPool().getQueue().size() > 1000){
                        continue;
                    }
                    String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
                    if (zhiHuDao1.insertUrl(cn, Md5Util.Convert2Md5(nextUrl)) ||
                            zhiHuHttpClient.getDetailListPageThreadPool().getActiveCount() == 1){
                        //防止死锁
                        HttpGet request = new HttpGet(nextUrl);
                        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                        zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
                    }
                }
            }
            else if(!Config.dbEnable || zhiHuHttpClient.getDetailListPageThreadPool().getActiveCount() == 1){
                parseUserCount.incrementAndGet();
                for (int j = 0; j < u.getFollowees() / 20; j++){
                    String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                    zhiHuHttpClient.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
                }
            }
        }
    }

    /**
     * 每个thread维护一个Connection
     * @return
     */
    private Connection getConnection(){
        Thread currentThread = Thread.currentThread();
        Connection cn = null;
        if (!connectionMap.containsKey(currentThread)){
            cn = ConnectionManager.createConnection();
            connectionMap.put(currentThread, cn);
        }  else {
            cn = connectionMap.get(currentThread);
        }
        return cn;
    }

    public static Map<Thread, Connection> getConnectionMap() {
        return connectionMap;
    }

}
