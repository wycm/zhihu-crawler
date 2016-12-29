package com.crawl.zhihu;

import com.crawl.core.util.Config;
import com.crawl.zhihu.dao.ConnectionManager;
import com.crawl.zhihu.dao.ZhiHuDAO;
import com.crawl.core.util.Constants;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.SimpleLogger;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.zhihu.task.DownloadTask;
import com.crawl.zhihu.task.ParseTask;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZhiHuHttpClient extends HttpClient{
    private static Logger logger = SimpleLogger.getSimpleLogger(ZhiHuHttpClient.class);
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
    /**
     * request　header
     */
    private static String authorization;
    public ZhiHuHttpClient() {
        initHttpClient();
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(downloadThreadExecutor, "DownloadPage ThreadPool")).start();
        new Thread(new ThreadPoolMonitor(parseThreadExecutor, "ParsePage ThreadPool")).start();
    }
    /**
     * 初始化HttpClient
     */
    @Override
    public void initHttpClient() {
        if(Config.crawlStrategy.equals(Constants.LOGIN_PARSE_STRATEGY) &&
                !deserializeCookieStore(Config.cookiePath)){
            /**
             * 模拟登录知乎，持久化Cookie到本地
             * 不用以后每次都登录
             */
            new ModelLogin().login(Config.emailOrPhoneNum, Config.password);
        }else if (Config.crawlStrategy.equals(Constants.TOURIST_PARSE_STRATEGY)){
            /**
             * 获取 authorization 字段值
             */
            setAuthorization();
        }
        if(Config.dbEnable){
            ZhiHuDAO.DBTablesInit(ConnectionManager.getConnection());
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
    private void setAuthorization(){
        String content = null;
        try {
            content = HttpClientUtil.getWebPage(Config.startURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("https://static\\.zhihu\\.com/heifetz/main\\.app\\.([0-9]|[a-z])*\\.js");
        Matcher matcher = pattern.matcher(content);
        String jsSrc = null;
        if (matcher.find()){
            jsSrc = matcher.group(0);
        }
        String jsContent = null;
        try {
            jsContent = HttpClientUtil.getWebPage(jsSrc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pattern = Pattern.compile("CLIENT_ALIAS=\"(([0-9]|[a-z])*)\"");
        matcher = pattern.matcher(jsContent);
        if (matcher.find()){
            authorization = matcher.group(1);
            return ;
        }
        throw new RuntimeException("not get authorization");
    }
    public static String getAuthorization(){
        return authorization;
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
