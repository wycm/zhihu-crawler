package com.crawl.proxy.task;

import com.crawl.core.util.Constants;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 代理检测task
 * 通过访问知乎首页，能否正确响应
 * 将可用代理添加到DelayQueue延时队列中
 */
public class ProxyTestTask implements Runnable{
    private final static Logger logger = Logger.getLogger(ProxyTestTask.class);
    private Proxy proxy;
    public ProxyTestTask(Proxy proxy){
        this.proxy = proxy;
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        HttpGet request = new HttpGet(Constants.INDEX_URL);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                    setConnectTimeout(Constants.TIMEOUT).
                    setConnectionRequestTimeout(Constants.TIMEOUT).
                    setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
            request.setConfig(requestConfig);
            Page page = ZhiHuHttpClient.getInstance().getWebPage(request);
            long endTime = System.currentTimeMillis();
            String logStr = String.format("%s %s %s %s %s %s %d %s %d%s", Thread.currentThread().getName(), this.getClass().getName(),
                    proxy, "executing request", page.getUrl(), "response statusCode:", page.getStatusCode(),
                    "request cost time:", (endTime - startTime), "ms");
            if (page == null || page.getStatusCode() != 200){
                logger.warn(logStr);
                return;
            }
            request.releaseConnection();

            logger.debug(proxy.toString() + "---------" + page.toString());
            if(!ProxyPool.proxySet.contains(proxy)){
                logger.debug(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
                ProxyPool.lock.writeLock().lock();
                try {
                    ProxyPool.proxySet.add(proxy);
                } finally {
                    ProxyPool.lock.writeLock().unlock();
                }
                ProxyPool.proxyQueue.add(proxy);
            }
        } catch (IOException e) {
            logger.debug("IOException:", e);
        } finally {
            if (request != null){
                request.releaseConnection();
            }
        }
    }
    private String getProxyStr(){
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
