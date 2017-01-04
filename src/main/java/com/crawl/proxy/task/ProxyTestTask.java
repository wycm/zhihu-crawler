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
 * 检测代理是否可用
 * 通过访问知乎首页，能否正确响应
 * 将可用代理添加到DelayQueue延时队列中，设置５秒延迟
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
        proxy.setLastUseTime(startTime);
        try {
            HttpGet request = new HttpGet(Constants.INDEX_URL);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).
                    setConnectTimeout(10000).
                    setConnectionRequestTimeout(10000).
                    setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
            request.setConfig(requestConfig);
            Page page = ZhiHuHttpClient.getInstance().getWebPage(request);
            if (page == null || page.getStatusCode() != 200){
                return;
//                throw new IOException("status code " + page.getStatusCode());
            }
            request.releaseConnection();
            long endTime = System.currentTimeMillis();
            if(!ProxyPool.proxySet.contains(getProxyStr())){
                ProxyPool.proxySet.add(getProxyStr());
                ProxyPool.proxyQueue.add(proxy);
            }
            logger.debug(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
    private String getProxyStr(){
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
