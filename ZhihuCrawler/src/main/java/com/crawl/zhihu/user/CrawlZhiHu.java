package com.crawl.zhihu.user;

import com.crawl.dao.ConnectionManage;
import com.crawl.dao.ZhihuDAO;
import com.crawl.util.MyLogger;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 爬虫入口
 */
public class CrawlZhiHu {
    private static Logger logger = MyLogger.getMyLogger(CrawlZhiHu.class);
    private Storage storage = null;
    public CrawlZhiHu(){
        storage = new Storage();
    }
    public static void main(String[] args) throws Exception{
        ZhihuHttpClient zhClient = new ZhihuHttpClient();
        CrawlZhiHu crawlZhiHu= new CrawlZhiHu();
        //初始化数据库表
        ZhihuDAO.DBTablesInit(ConnectionManage.getConnection());
        crawlZhiHu.getZhiHu(zhClient, "https://www.zhihu.com/people/wo-yan-chen-mo");
    }
    /**
     * @param zhClient 知乎HttpClient客户端
     * @param startUrl 爬虫开始的url
     * @throws Exception
     */
    public void getZhiHu(ZhihuHttpClient zhClient, String startUrl){
        System.out.print("请输入要抓取的用户数量:");
        int crawlUserCount = new Scanner(System.in).nextInt();
        ThreadPoolMonitor et1,et2;//监测线程池执行情况
        // 构造一个获取网页线程池
        ThreadPoolExecutor getWebPagethreadPool = new ThreadPoolExecutor(8, 10, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy());
        //构造一个解析网页线程池
        MyThreadPoolExecutor parseWebPagethreadPool = new MyThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy(),storage);
        HttpGet getRequest = new HttpGet(startUrl);
        getWebPagethreadPool.execute(new GetWebPageTask(zhClient,getRequest,storage,getWebPagethreadPool,parseWebPagethreadPool));
        et1 = new ThreadPoolMonitor(parseWebPagethreadPool,"解析网页线程池--");
        et2 = new ThreadPoolMonitor(getWebPagethreadPool,"获取网页线程池--");
        new Thread(et1).start();
        new Thread(et2).start();
        while(true){
            if(ParseWebPageTask.userCount >= crawlUserCount){
                getWebPagethreadPool.shutdown();
                if(getWebPagethreadPool.isTerminated()  && storage.getQueue().size() == 0){
                    //获取网页线程池任务完成,并且仓库任务为0时，终止解析网页线程池
                    parseWebPagethreadPool.shutdown();
                    et1.shutdown();
                    et2.shutdown();
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("InterruptedException",e);
            }
        }
    }
}
