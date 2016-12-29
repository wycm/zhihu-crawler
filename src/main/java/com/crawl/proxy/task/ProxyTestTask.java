package com.crawl.proxy.task;

import com.crawl.core.util.HttpClientUtil;
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


public class ProxyTestTask implements Runnable{
    private final static Logger logger = Logger.getLogger(ProxyTestTask.class);
    private Proxy proxy;
    public ProxyTestTask(Proxy proxy){
        this.proxy = proxy;
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        proxy.setLastUseTime(startTime);
        try {
            HttpGet request = new HttpGet("https://www.zhihu.com/people/wo-yan-chen-mo/following");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).
                    setConnectTimeout(10000).
                    setConnectionRequestTimeout(10000).
                    setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
            request.setConfig(requestConfig);
            Page page = ZhiHuHttpClient.getInstance().getWebPage(request);
            if (page == null && page.getStatusCode() != 200){
                return;
//                throw new IOException("status code " + page.getStatusCode());
            }
            request.releaseConnection();
            long endTime = System.currentTimeMillis();
            proxy.setDelay(5000);
            if(!ProxyPool.proxySet.contains(getProxyStr())){
                ProxyPool.queue.add(proxy);
            }
            logger.info(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
    private String getProxyStr(){
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
