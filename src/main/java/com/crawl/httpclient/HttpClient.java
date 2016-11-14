package com.crawl.httpclient;

import com.crawl.entity.Page;
import com.crawl.util.HttpClientUtil;
import com.crawl.util.MyLogger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangwang on 16-8-19.
 */
public abstract class HttpClient {
    private Logger logger = MyLogger.getLogger(HttpClient.class);
    private static HttpClient httpClient;
    protected CloseableHttpClient closeableHttpClient;
    protected HttpClientContext httpClientContext;
    private CloseableHttpResponse closeableHttpResponse;
    protected HttpClient(){
        this.closeableHttpClient = HttpClientUtil.getMyHttpClient();
        this.httpClientContext = HttpClientUtil.getMyHttpClientContext();
    }
    private CloseableHttpResponse getResponse(HttpRequestBase request){
        try {
            closeableHttpResponse = closeableHttpClient.execute(request, httpClientContext);
            return closeableHttpResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public InputStream getWebPageInputStream(String url){
        HttpGet httpGet = new HttpGet(url);
        return getWebPageInputStream(httpGet);
    }

    public InputStream getWebPageInputStream(HttpRequestBase request){
        try {
            HttpEntity httpEntity = getResponse(request).getEntity();
            return httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWebPageContent(String url){
        InputStream is = getWebPageInputStream(url);
        if(is != null){
            try {
                return IOUtils.toString(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Page getWebPage(String url){
        return getWebPag(new HttpGet(url));
    }
    public Page getWebPag(HttpRequestBase request){
        Page page = new Page();
        CloseableHttpResponse response = getResponse(request);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(request.getURI().toString());
        try {
            if(page.getStatusCode() == 200){
                page.setHtml(IOUtils.toString(response.getEntity().getContent()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.releaseConnection();
        }
        return page;
    }
    /**
     * 反序列化CookiesStore
     * @return
     */
    public boolean deserializeCookieStore(String path){
        try {
            CookieStore cookieStore = (CookieStore) HttpClientUtil.deserializeMyHttpClient(path);
            httpClientContext.setCookieStore(cookieStore);
        } catch (Exception e){
            logger.warn("反序列化Cookie失败,没有找到Cookie文件");
            return false;
        }
        return true;
    }
    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    public abstract void initHttpClient();
}
