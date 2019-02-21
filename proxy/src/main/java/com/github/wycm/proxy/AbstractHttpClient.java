package com.github.wycm.proxy;

import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.SimpleHttpClient;
import com.github.wycm.proxy.util.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Slf4j
public abstract class AbstractHttpClient {
    @Autowired
    protected ProxyQueue proxyQueue;

    @Autowired
    protected CommonProperties commonProperties;

    protected SimpleHttpClient httpClient;


    public Page asyncGet(String url) throws ExecutionException, InterruptedException {
        Response response = httpClient.getResponse(url, null);
        return asyncGet(url, response);
    }
    public Page asyncGet(String url, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        if (userAgent == null){
            return asyncGet(url);
        }
        Response response = httpClient.getResponse(url, null, userAgent, headers);
        return asyncGet(url, response);
    }

    public Page asyncGet(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        Response response = httpClient.getResponse(url, proxyServer);
        return asyncGet(url, response);
    }

    public Page asyncGet(String url, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
        if (userAgent == null){
            return asyncGet(url, proxyServer);
        }
        Response response = httpClient.getResponse(url, proxyServer, userAgent, headers);
        return asyncGet(url, response);
    }

    public Page asyncGet(String url, Response response) throws ExecutionException, InterruptedException {
        Page page = new Page();
        page.setStatusCode(response.getStatusCode());
        page.setHtml(response.getResponseBody());
        page.setUrl(url);
        return page;
    }

    public Page executeRequestWithRetry(CrawlerMessage crawlerMessage, Function<String, Boolean> ipBannedFunction) throws InterruptedException {
        int currentRetryTimes = 0;
        Proxy currentProxy = null;
        while (currentRetryTimes < 3){
            Page page = null;
            try {
                long requestStartTime = 0l;
                currentProxy = proxyQueue.takeProxy(commonProperties.getTargetPageProxyQueueName());
                requestStartTime = System.currentTimeMillis();

                if(!(currentProxy.getIp().equals(getLocalIPService().getLocalIp()))){
                    //代理
                    page = this.asyncGet(crawlerMessage.getUrl(), new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                } else {
                    page = this.asyncGet(crawlerMessage.getUrl(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
                }
                long requestEndTime = System.currentTimeMillis();
                page.setProxy(currentProxy);
                int status = page.getStatusCode();

                if(status == 200){
                    if (ipBannedFunction.apply(page.getHtml())){
                        log.warn("response error, url:{}, response:{}, discard ip:{}, port:{}", crawlerMessage.getUrl(), page.getHtml(), currentProxy.getIp(), currentProxy.getPort());
                        throw new ExecutionException(new Throwable("response error"));
                    }
                    ProxyUtil.handleResponseSuccProxy(currentProxy);
                    return page;
                } else {
                    log.error("{}, retryTimes:{},{}, url:{}, response Code:{}, cost time:{}ms"
                            , Thread.currentThread().getName()
                            , currentRetryTimes
                            , currentProxy
                            , page.getUrl()
                            , status
                            , requestEndTime - requestStartTime);
                    ProxyUtil.handleResponseFailedProxy(currentProxy);
                }
            } catch (ExecutionException e) {
                log.debug(e.getMessage(), e);
                ProxyUtil.handleResponseFailedProxy(currentProxy);
            } finally {
                if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
                    proxyQueue.addProxy(commonProperties.getTargetPageProxyQueueName(), currentProxy);
                }
            }
            currentRetryTimes = currentRetryTimes + 1;
        }
        return null;
    }

    public abstract LocalIPService getLocalIPService();

}
