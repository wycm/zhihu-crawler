package com.crawl.proxy;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.xicidaili.XicidailiProxyListPageParser;
import com.crawl.proxy.task.ProxyTestTask;
import com.crawl.zhihu.HttpClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProxyHttpClient extends HttpClient{
    private static class ProxyHttpClientHolder {
        private static final ProxyHttpClient INSTANCE = new ProxyHttpClient();
    }
    public static final ProxyHttpClient getInstance(){
        return ProxyHttpClientHolder.INSTANCE;
    }
    /**
     * 代理测试线程池
     */
    private ThreadPoolExecutor proxyTestThreadExecutor;
    public ProxyHttpClient(){
        initHttpClient();
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTest ThreadPool")).start();
    }
    @Override
    public void initHttpClient() {

    }
    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        proxyTestThreadExecutor = new ThreadPoolExecutor(50, 50,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
    public void startCrawl(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String content = null;
                    try {
                        content = HttpClientUtil.getWebPage(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    List<Proxy> proxyList = new XicidailiProxyListPageParser().parse(content);
                    for(Proxy p : proxyList){
                        proxyTestThreadExecutor.execute(new ProxyTestTask(p));
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
