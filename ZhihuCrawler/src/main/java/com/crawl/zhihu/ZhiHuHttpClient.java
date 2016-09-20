package com.crawl.zhihu;

import com.crawl.config.Config;
import com.crawl.dao.ConnectionManage;
import com.crawl.dao.ZhiHuDAO;
import com.crawl.httpclient.HttpClient;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.task.DownloadTask;
import com.crawl.zhihu.task.ParseTask;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class ZhiHuHttpClient extends HttpClient{
    Logger logger = Logger.getLogger(ZhiHuHttpClient.class);
    private static class ZhiHuHttpClientHolder {
        private static final ZhiHuHttpClient INSTANCE = new ZhiHuHttpClient();
    }
    public static final ZhiHuHttpClient getInstance(){
        return ZhiHuHttpClientHolder.INSTANCE;
    }
    private static ZhiHuHttpClient zhiHuHttpClient;
    /**
     * 解析网页线程池
     */
    private ThreadPoolExecutor parseThreadExecutor;
    /**
     * 下载网页线程池
     */
    private ThreadPoolExecutor downloadThreadExecutor;
    public ZhiHuHttpClient() {
        initHttpClient();
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(downloadThreadExecutor, "DownloadPage ThreadPool")).start();
        new Thread(new ThreadPoolMonitor(parseThreadExecutor, "ParsePage ThreadPool")).start();
    }
    /**
     * 初始化知乎客户端
     * 模拟登录知乎，持久化Cookie到本地
     * 不用以后每次都登录
     */
    @Override
    public void initHttpClient() {
        Properties properties = new Properties();
        if(!deserializeCookieStore(Config.cookiePath)){
            new ModelLogin().login(this, Config.emailOrPhoneNum, Config.password);
        }
        if(Config.dbEnable){
            ZhiHuDAO.DBTablesInit(ConnectionManage.getConnection());
        }
    }

    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        parseThreadExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        downloadThreadExecutor = new ThreadPoolExecutor(Config.downloadThreadSize,
                Config.downloadThreadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    public void startCrawl(String url){
        downloadThreadExecutor.execute(new DownloadTask(url));
        manageZhiHuClient();
    }

    /**
     * 管理知乎客户端
     * 关闭整个爬虫
     */
    public void manageZhiHuClient(){
        while (true) {
            /**
             * 下载网页数
             */
            long downloadPageCount = downloadThreadExecutor.getTaskCount();
            if (downloadPageCount >= Config.downloadPageCount &&
                    ! ZhiHuHttpClient.getInstance().getDownloadThreadExecutor().isShutdown()) {
                ParseTask.isStopDownload = true;
                /**
                 * shutdown 下载网页线程池
                 */
                ZhiHuHttpClient.getInstance().getDownloadThreadExecutor().shutdown();
            }
            if(ZhiHuHttpClient.getInstance().getDownloadThreadExecutor().isTerminated() &&
                    !ZhiHuHttpClient.getInstance().getParseThreadExecutor().isShutdown()){
                /**
                 * 下载网页线程池关闭后，再关闭解析网页线程池
                 */
                ZhiHuHttpClient.getInstance().getParseThreadExecutor().shutdown();
            }
            if(ZhiHuHttpClient.getInstance().getParseThreadExecutor().isTerminated()){
                /**
                 * 关闭线程池Monitor
                 */
                ThreadPoolMonitor.isStopMonitor = true;
                logger.info("--------------爬取结果--------------");
                logger.info("获取用户数:" +ParseTask.parseUserCount.get());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public ThreadPoolExecutor getParseThreadExecutor() {
        return parseThreadExecutor;
    }

    public ThreadPoolExecutor getDownloadThreadExecutor() {
        return downloadThreadExecutor;
    }
}
