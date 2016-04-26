package com.crawl.zhihu.crawlimganswer;

import com.crawl.util.MyLogger;
import com.crawl.util.ThreadPoolMonitor;
import com.crawl.util.chcUtils;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 获取用户带有图片的答案
 */
public class CrawlUserImgAnswer {
    private static Logger logger = MyLogger.getMyLogger(CrawlUserImgAnswer.class);
    private static ZhihuHttpClient zhClient = new ZhihuHttpClient();
    private static ImgAnswerStorage storage = new ImgAnswerStorage();

    public static void main(String args[]) {
        CrawlUserImgAnswer guia = new CrawlUserImgAnswer();
        guia.crawlUserImgAnswer();
    }

    /**
     * 获取用户回答数
     *
     * @param username 用户唯一标识
     */
    public int getAnswersCount(ZhihuHttpClient zhClient, String username) {
        int answerCount = 0;
        String url = "https://www.zhihu.com/people/" + username + "/answers";
        HttpGet httpGet = new HttpGet(url);
        String s = chcUtils.getWebPage(zhClient.getHttpClient(), zhClient.getContext(), httpGet, "utf-8", false);
        Document doc = Jsoup.parse(s);
        answerCount = Integer.valueOf(doc.select(".zm-profile-header.ProfileCard a.item.active span").first().text());
        return answerCount;
    }

    public void crawlUserImgAnswer() {
        String userId = "cici";
        int answers = getAnswersCount(zhClient, userId);
        // 构造一个获取网页线程池
        ThreadPoolExecutor getWebPagethreadPool = new ThreadPoolExecutor(8, 8, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardOldestPolicy());
        //构造一个解析网页线程池
        MyThreadPoolExecutor parseImgAnswerthreadPool = new MyThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardOldestPolicy(),storage);
        for (int i = 1; i <= answers / 20 + 1; i++) {
            HttpGet getRequest = new HttpGet("https://www.zhihu.com/people/" + userId + "/answers?page=" + i);
            getWebPagethreadPool.execute(new GetWebPageTask(zhClient, getRequest, storage, getWebPagethreadPool, parseImgAnswerthreadPool));
        }
        ThreadPoolMonitor et1,et2;
        et1 = new ThreadPoolMonitor(getWebPagethreadPool,"获取网页线程池--");
        et2 = new ThreadPoolMonitor(parseImgAnswerthreadPool,"解析网页线程池--");
        new Thread(et1).start();//用一个线程来监视，该线程池执行情况，方便调整，以达到最大效率
        new Thread(et2).start();
        while(true){
            if(getWebPagethreadPool.getActiveCount() == 0 && storage.getQueue().size() == 0){
                getWebPagethreadPool.shutdown();
                if(getWebPagethreadPool.isTerminated()  && storage.getQueue().size() == 0){
                    //获取网页线程池任务完成,并且仓库任务为0时，终止解析线程池
                    parseImgAnswerthreadPool.shutdown();
                    et1.shutdown();
                    et2.shutdown();
                    break;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}