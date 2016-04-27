package com.crawl.zhihu.crawlimganswer;

import com.crawl.util.MyLogger;
import com.crawl.entity.Href;
import org.apache.log4j.Logger;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/4/9 0009.
 * 重写terminated方法
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor{
    private static Logger logger = MyLogger.getMyLogger(MyThreadPoolExecutor.class);
    private long startTime;
    private ImgAnswerStorage storage;
    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        startTime = System.currentTimeMillis();
    }
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime, TimeUnit unit,
                                BlockingQueue<Runnable> workQueue,
                                RejectedExecutionHandler handler,
                                ImgAnswerStorage storage) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.storage = storage;
        startTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void terminated(){
        long endTime = System.currentTimeMillis();
        Vector<Href> vs = storage.getResult();
        System.out.println("带图片的回答数:" + vs.size());
        for (Href h:vs){
            System.out.println(h.getHref() + h.getTitle());
        }
        System.out.println("----总共耗时:" + (endTime - startTime) + "ms");
    }
    public static void main(String args []) throws Exception {
    }
}
