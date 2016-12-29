package com.crawl.proxy.task;


import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyListPageParser;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.xicidaili.XicidailiProxyListPageParser;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.DelayQueue;

public class ProxyManager implements Runnable{
    private final static Logger logger = Logger.getLogger(ProxyManager.class);
    public final static DelayQueue queue = new DelayQueue();
    private String url;
    private ProxyListPageParser proxyListPageParser;
    public ProxyManager(String url, ProxyListPageParser proxyListPageParser){
        this.url = url;
        this.proxyListPageParser = proxyListPageParser;
    }
    @Override
    public void run() {
        while (true){
            String content = null;
            try {
                content = HttpClientUtil.getWebPage(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Proxy> proxyList = proxyListPageParser.parse(content);
            for(Proxy p : proxyList){
                long startTime = System.currentTimeMillis();
                p.setLastUseTime(startTime);
                try {
                    HttpGet request = new HttpGet("https://www.zhihu.com");
                    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).
                            setConnectTimeout(10000).
                            setConnectionRequestTimeout(10000).
                            setProxy(new HttpHost(p.getIp(), p.getPort())).
                            setCookieSpec(CookieSpecs.STANDARD).
                            build();
                    request.setConfig(requestConfig);
                    HttpClientUtil.getWebPage(request);
                    long endTime = System.currentTimeMillis();
                    logger.info(p.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
                    p.setDelay(5000);
                    queue.add(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        new Thread(new ProxyManager("http://www.xicidaili.com/wt", new XicidailiProxyListPageParser())).start();
    }
}
