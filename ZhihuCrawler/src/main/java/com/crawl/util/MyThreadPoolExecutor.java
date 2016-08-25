package com.crawl.util;

import com.crawl.dao.ConnectionManage;
import com.crawl.zhihu.Storage;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/4/9 0009.
 * 重写terminated方法，用户统计最后结果
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor{
    private static Logger logger = MyLogger.getMyLogger(MyThreadPoolExecutor.class);
    private long startTime;
    private Storage storage;
    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        startTime = System.currentTimeMillis();
    }
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime, TimeUnit unit,
                                BlockingQueue<Runnable> workQueue,
                                RejectedExecutionHandler handler,
                                Storage storage) {
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
        ConnectionManage.close();
        logger.info("----总共耗时:" + (endTime - startTime) + "ms");
    }
}
