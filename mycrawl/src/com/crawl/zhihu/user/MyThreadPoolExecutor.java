package com.crawl.zhihu.user;

import com.crawl.dao.ConnectionManage;
import com.crawl.entity.User;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;

import java.util.Vector;
import java.util.concurrent.*;

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
        logger.info("获取网页数:" + GetWebPageTask.gwpCount);
        logger.info("解析网页数:" + ParseWebPageTask.pwpCount);
        ConnectionManage.close();
        logger.info("----总共耗时:" + (endTime - startTime) + "ms");
    }
}
