package com.crawl.proxy;

import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.ProxyListPageParserFactory;
import com.crawl.proxy.task.ProxyTestTask;
import com.crawl.core.httpclient.AbstractHttpClient;
import com.crawl.zhihu.entity.Page;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProxyHttpClient extends AbstractHttpClient {
    private static final Logger logger = Logger.getLogger(ProxyHttpClient.class);
    private volatile static ProxyHttpClient instance;
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
    public ProxyHttpClient(){
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTest ThreadPool")).start();
    }
    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        proxyTestThreadExecutor = new ThreadPoolExecutor(50, 50,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    public void startCrawl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String content = null;
                    for (String url : ProxyPool.proxyMap.keySet()){
                        try {
                            Page page = getWebPage(url);
                            ProxyListPageParser parser = ProxyListPageParserFactory.getProxyListPageParser(ProxyPool.proxyMap.get(url));
                            List<Proxy> proxyList = parser.parse(page.getHtml());
                            for(Proxy p : proxyList){
                                proxyTestThreadExecutor.execute(new ProxyTestTask(p));
                            }
                            Thread.sleep(1000);
                        } catch (IOException e) {
                            logger.error(e);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000 * 60 * 30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
