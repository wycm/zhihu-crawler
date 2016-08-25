package com.crawl.util;

import org.apache.log4j.Logger;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类，监视ThreadPoolExecutor执行情况
 */
public class ThreadPoolMonitor implements Runnable{
    private static Logger logger = MyLogger.getMyLogger(ThreadPoolMonitor.class);
    private ThreadPoolExecutor executor;
    private volatile boolean run=true;
    private String name = "";
    public ThreadPoolMonitor(ThreadPoolExecutor executor,String name){
        this.executor = executor;
        this.name = name;
    }
    public void shutdown(){
        this.run=false;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void run(){
        while(run){
            logger.info(name +
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, queueSize: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getQueue().size(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));
            if(this.executor.isShutdown() == true){
                shutdown();
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