package com.crawl.proxy;

import com.crawl.util.ThreadPoolMonitor;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/4/23 0023.
 */
public class ProxyTest {
    public static void main(String args []){

        ZhihuHttpClient zhClient = new ZhihuHttpClient();
        HttpHost proxy = new HttpHost("175.17.38.118", 80, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        // 构造一个获取网页线程池
        ThreadPoolExecutor getWebPagethreadPool = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.DiscardOldestPolicy());
        ThreadPoolMonitor et1 = new ThreadPoolMonitor(getWebPagethreadPool,"获取网页线程池--");
        new Thread(et1).start();
        for(int i = 0;i <= 50;i++){
            getWebPagethreadPool.execute(new GetWebPageTask(zhClient,new HttpGet("https://www.zhihu.com/people/wo-yan-chen-mo/followees")));
        }
        while (true){
            if(getWebPagethreadPool.getActiveCount() == 0 && getWebPagethreadPool.getCompletedTaskCount() > 0){
                getWebPagethreadPool.shutdown();
                if(getWebPagethreadPool.isShutdown()){
                    et1.setRun(false);
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
