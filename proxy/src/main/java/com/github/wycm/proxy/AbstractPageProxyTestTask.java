package com.github.wycm.proxy;

import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.proxy.ProxyServer;

import java.util.concurrent.ExecutionException;


@Slf4j
@NoArgsConstructor
public abstract class AbstractPageProxyTestTask implements Runnable {

    protected Proxy proxy;

    private String url;

    public AbstractPageProxyTestTask(Proxy proxy, String url){
        this.proxy = proxy;
        this.url = url;
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            Page page = null;
            if (StringUtils.isBlank(proxy.getIp())){
                page = getHttpClient().asyncGet(url);
            } else {
                page = getHttpClient().asyncGet(url, new ProxyServer.Builder(proxy.getIp(), proxy.getPort()).build());
            }
            long endTime = System.currentTimeMillis();
            String logStr = Thread.currentThread().getName() + " " + proxy.getProxyStr() +
                    "  executing request " + page.getUrl()  + " response statusCode:" + page.getStatusCode() +
                    "  request cost time:" + (endTime - startTime) + "ms";
            if (responseError(page)){
                log.warn(logStr);
                throw new ExecutionException(new Throwable("response error"));
            }
            log.debug(proxy.toString() + "---------" + page.toString());
            log.debug(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
            getProxyQueue().addProxy(getProxyQueueName(), proxy);
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        } catch (ExecutionException e) {
            log.debug(e.getMessage(), e);
        } finally {
            try {
                receiveNewTask();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }
    }
    protected void receiveNewTask() throws InterruptedException {
        if (!Constants.stopService){
            Proxy proxy = getTaskQueueService().receiveProxyTask(CrawlerUtils.getTaskQueueName(this.getClass()));
            createNewTask(proxy, getTestUrl());
        }
    }

    /**
     *
     * @return
     */
    protected abstract String getTestUrl();

    protected abstract String getProxyQueueName();

    protected abstract AbstractHttpClient getHttpClient();

    protected abstract ProxyQueue getProxyQueue();

    protected abstract TaskQueueService getTaskQueueService();

    /**
     * 创建新任务
     */
    protected abstract void createNewTask(Proxy proxy, String url);

    private String getProxyStr(){
        return proxy.getIp() + ":" + proxy.getPort();
    }

    protected boolean responseError(Page page){
        return page == null || page.getStatusCode() != 200;
    }
}
