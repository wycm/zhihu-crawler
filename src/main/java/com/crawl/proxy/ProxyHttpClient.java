package com.crawl.proxy;

import com.crawl.core.util.Config;
import com.crawl.core.util.Constants;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.task.ProxyPageTask;
import com.crawl.core.httpclient.AbstractHttpClient;
import com.crawl.proxy.task.ProxySerializeTask;
import com.crawl.zhihu.entity.Page;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProxyHttpClient extends AbstractHttpClient {
    private static final Logger logger = Logger.getLogger(ProxyHttpClient.class);
    private volatile static ProxyHttpClient instance;
    public static Set<Page> downloadFailureProxyPageSet = new HashSet<>(ProxyPool.proxyMap.size());

    public static ProxyHttpClient getInstance(){
        if (instance == null){
            synchronized (ProxyHttpClient.class){
                if (instance == null){
                    instance = new ProxyHttpClient();
                }
            }
        }
        return instance;
    }
    /**
     * 代理测试线程池
     */
    private ThreadPoolExecutor proxyTestThreadExecutor;
    /**
     * 代理网站下载线程池
     */
    private ThreadPoolExecutor proxyDownloadThreadExecutor;
    public ProxyHttpClient(){
        initThreadPool();
        initProxy();
    }
    /**
     * 初始化线程池
     */
    private void initThreadPool(){
        proxyTestThreadExecutor = new ThreadPoolExecutor(100, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.DiscardPolicy());
        proxyDownloadThreadExecutor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTestThreadPool")).start();
        new Thread(new ThreadPoolMonitor(proxyDownloadThreadExecutor, "ProxyDownloadThreadExecutor")).start();
    }

    /**
     * 初始化proxy
     *
     */
    private void initProxy(){
        Proxy[] proxyArray = null;
        try {
            proxyArray = (Proxy[]) HttpClientUtil.deserializeObject(Config.proxyPath);
            for (Proxy p : proxyArray){
                if (p == null){
                    continue;
                }
                p.setTimeInterval(Constants.TIME_INTERVAL);
                p.setFailureTimes(0);
                p.setSuccessfulTimes(0);
                ProxyPool.proxyQueue.add(p);
                ProxyPool.proxySet.add(p);
            }
            logger.info("反序列化proxy成功，" + proxyArray.length + "个代理");
        } catch (Exception e) {
            logger.warn("反序列化proxy失败");
        }
    }
    /**
     * 抓取代理
     */
    public void startCrawl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (String url : ProxyPool.proxyMap.keySet()){
                        /**
                         * 首次本机直接下载代理页面
                         */
                        proxyDownloadThreadExecutor.execute(new ProxyPageTask(url, false));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000 * 60 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new ProxySerializeTask()).start();
    }
    public ThreadPoolExecutor getProxyTestThreadExecutor() {
        return proxyTestThreadExecutor;
    }

    public ThreadPoolExecutor getProxyDownloadThreadExecutor() {
        return proxyDownloadThreadExecutor;
    }
}