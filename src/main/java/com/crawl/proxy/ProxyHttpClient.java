package com.crawl.proxy;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.ThreadPoolMonitor;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.ProxyListPageParserFactory;
import com.crawl.proxy.task.ProxyTestTask;
import com.crawl.core.httpclient.AbstractHttpClient;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
    public ProxyHttpClient(){
        intiThreadPool();
        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTest ThreadPool")).start();
    }
    /**
     * 初始化线程池
     */
    private void intiThreadPool(){
        proxyTestThreadExecutor = new ThreadPoolExecutor(100, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 抓取代理
     * 此处还需改进
     */
    public void startCrawl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (String url : ProxyPool.proxyMap.keySet()){
                        handleProxyPage(new HttpGet(url));
                    }
                    /**
                     * 处理下载失败的代理page
                     * 从ProxyPool中取代理去获取代理page
                     */
                    for (Page page : downloadFailureProxyPageSet){
                        if (page.getStatusCode() != 200){
                            try {
                                Proxy proxy = ProxyPool.proxyQueue.take();
                                while (true){
                                    if (proxy instanceof Direct){
                                        proxy.setTimeInterval(1000);
                                        ProxyPool.proxyQueue.add(proxy);
                                        continue;
                                    }
                                    break;
                                }
                                HttpGet request = new HttpGet(page.getUrl());
                                RequestConfig.Builder builder = HttpClientUtil.getRequestConfigBuilder();
                                request.setConfig(builder.setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).build());
                                handleProxyPage(request);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
    private void handleProxyPage(HttpRequestBase requestBase){
        Page page = null;
        try {
            page = getWebPage(requestBase);
            ProxyListPageParser parser = ProxyListPageParserFactory.
                    getProxyListPageParser(ProxyPool.proxyMap.get(requestBase.getURI().toURL().toString()));
            List<Proxy> proxyList = parser.parse(page.getHtml());
            for(Proxy p : proxyList){
                if(!ZhiHuHttpClient.getInstance().getDetailPageThreadPool().isTerminated()){
                    proxyTestThreadExecutor.execute(new ProxyTestTask(p));
                }
            }
            Thread.sleep(1000);
        } catch (IOException e) {
            logger.error("IOException", e);
            if(page == null){
                page = new Page();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            logger.error("Exception", e);
        } finally {
            if (page.getStatusCode() == 200){
                if(downloadFailureProxyPageSet.contains(page)){
                    downloadFailureProxyPageSet.remove(page);
                }
            } else {
                downloadFailureProxyPageSet.add(page);
            }
        }
        
    }
    public ThreadPoolExecutor getProxyTestThreadExecutor() {
        return proxyTestThreadExecutor;
    }
}