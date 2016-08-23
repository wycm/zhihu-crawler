package com.crawl.entity;

import com.crawl.util.HttpClientUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangwang on 16-8-19.
 */
public class HttpClient {
    private static HttpClient httpClient;
    protected CloseableHttpClient closeableHttpClient;
    protected HttpClientContext httpClientContext;
    private CloseableHttpResponse closeableHttpResponse;
    protected HttpClient(){
        this.closeableHttpClient = HttpClientUtil.getMyHttpClient();
        this.httpClientContext = HttpClientUtil.getMyHttpClientContext();
    }

    public static HttpClient getInstance(){
        if(httpClient == null){
            httpClient = new HttpClient();
        }
        return httpClient;
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
        Page page = new Page();
        CloseableHttpResponse response = getResponse(new HttpGet(url));
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(url);
        try {
            page.setHtml(IOUtils.toString(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }
}
