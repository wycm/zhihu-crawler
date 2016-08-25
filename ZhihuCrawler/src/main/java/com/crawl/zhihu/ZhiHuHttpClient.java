package com.crawl.zhihu;

import com.crawl.config.Config;
import com.crawl.httpclient.HttpClient;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.task.DownloadTask;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class ZhiHuHttpClient extends HttpClient{
    Logger logger = Logger.getLogger(ZhiHuHttpClient.class);
    private static ZhiHuHttpClient zhiHuHttpClient;
    /**
     * 解析网页执行器
     */
    private ExecutorService parseThreadExecutor;
    /**
     * 下载网页执行器
     */
    private ThreadPoolExecutor downloadThreadExecutor;
    public ZhiHuHttpClient() {
        initHttpClient();
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(downloadThreadExecutor, "DownloadPage ThreadPool")).start();
    }

    public static ZhiHuHttpClient getInstance(){
        if(zhiHuHttpClient == null){
            zhiHuHttpClient = new ZhiHuHttpClient();
        }
        return zhiHuHttpClient;
    }
    /**
     * 初始化知乎客户端
     * 模拟登录知乎，持久化Cookie到本地
     * 不用以后每次都登录
     */
    @Override
    public void initHttpClient() {
        Properties properties = new Properties();
        if(!antiSerializeCookieStore("/zhihucookies")){
            new ModelLogin().login(this, Config.emailOrPhoneNum, Config.password);
        }
    }

    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        parseThreadExecutor = Executors.newSingleThreadExecutor();
        downloadThreadExecutor = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    public void startCrawl(String url){
        downloadThreadExecutor.execute(new DownloadTask(url));
    }

    public ExecutorService getParseThreadExecutor() {
        return parseThreadExecutor;
    }

    public ThreadPoolExecutor getDownloadThreadExecutor() {
        return downloadThreadExecutor;
    }
}
