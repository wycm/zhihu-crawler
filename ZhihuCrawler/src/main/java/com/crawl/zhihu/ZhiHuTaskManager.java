package com.crawl.zhihu;

import java.util.concurrent.*;

/**
 * Created by yangwang on 16-8-24.
 * 任务管理器
 */
public class ZhiHuTaskManager {
    /**
     * 解析网页执行器
     */
    private ExecutorService parseThreadExecutor;
    /**
     * 下载网页执行器
     */
    private ThreadPoolExecutor downloadThreadExecutor;


    public ZhiHuTaskManager(){
        parseThreadExecutor = Executors.newSingleThreadExecutor();
        downloadThreadExecutor = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    public void startCrawl(String userIndexUrl){

    }
}
