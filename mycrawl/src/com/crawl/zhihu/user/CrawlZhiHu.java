package com.crawl.zhihu.user;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.crawl.util.MyLogger;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 * 多线程获取知乎问题所有问题链接
 *
 */
public class CrawlZhiHu {
    private static Logger logger = MyLogger.getMyLogger(CrawlZhiHu.class);
    private Storage storage = null;
    public CrawlZhiHu(){
        storage = new Storage();
    }
    public static void main(String[] args) throws Exception
    {
        ZhihuHttpClient zhClient = new ZhihuHttpClient();
        CrawlZhiHu crawlZhiHu= new CrawlZhiHu();
        crawlZhiHu.getZhiHu(zhClient, "https://www.zhihu.com/people/wo-yan-chen-mo/followees");
    }
    /**
     * @param zhClient 知乎HttpClient客户端
     * @param startUrl
     * @throws Exception
     */
    public void getZhiHu(ZhihuHttpClient zhClient, String startUrl){
        System.out.print("请输入要抓取的用户数量:");
        int crawlUserCount = new Scanner(System.in).nextInt();
        ThreadPoolMonitor et1,et2;
        // 构造一个获取网页线程池
        ThreadPoolExecutor getWebPagethreadPool = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy());
        //构造一个解析网页线程池
        MyThreadPoolExecutor parseWebPagethreadPool = new MyThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy(),storage);
//        deleteHrefTable(ConnectionManage.getConnection());//先清空href表
        HttpGet getRequest = new HttpGet(startUrl);
        getWebPagethreadPool.execute(new GetWebPageTask(zhClient,getRequest,storage,getWebPagethreadPool,parseWebPagethreadPool));
        et1 = new ThreadPoolMonitor(parseWebPagethreadPool,"解析网页线程池--");
        et2 = new ThreadPoolMonitor(getWebPagethreadPool,"获取网页线程池--");
        new Thread(et1).start();//用一个线程来监视，该线程池执行情况，方便调整，以达到最大效率
        new Thread(et2).start();
        while(true){
            if(ParseWebPageTask.userCount >= crawlUserCount){
                getWebPagethreadPool.shutdown();
                if(getWebPagethreadPool.isTerminated()  && storage.getQueue().size() == 0){
                    //获取网页线程池任务完成,并且仓库任务为0时，终止解析线程池
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
