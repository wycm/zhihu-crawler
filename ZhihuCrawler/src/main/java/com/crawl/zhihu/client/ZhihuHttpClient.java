package com.crawl.zhihu.client;

import com.crawl.util.HttpClientUtil;
import com.crawl.util.MyLogger;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

/**
 * 知乎HttpClient客户端
 */
public class ZhihuHttpClient {
    private static Logger logger = MyLogger.getMyLogger(ZhihuHttpClient.class);
    private CloseableHttpClient httpClient = null;//http客户端
    private HttpClientContext context = null;//http上下文

    /**
     * 创建HttpClient和HttpClientContext，并反序列化Cookies
     */
    public ZhihuHttpClient(){
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();
        //方式1
//        this.httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();//这种并发连接数比默认CloseableHttpClient更多
        //方式2
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(15);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(100);//单路由最大并发数
        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .setConnectionManager(cm)
                .build();
        //方式3
//        this.httpClient = chcUtils.getMyHttpClient();
        this.context = HttpClientUtil.getMyHttpClientContext();
        //反序列化Cookie
        this.context.setCookieStore((CookieStore) HttpClientUtil.antiSerializeMyHttpClient("/zhihucookies"));
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClientContext getContext() {
        return context;
    }

    public void setContext(HttpClientContext context) {
        this.context = context;
    }
}
