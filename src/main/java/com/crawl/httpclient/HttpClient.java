package com.crawl.httpclient;

import com.crawl.entity.Page;
import com.crawl.util.HttpClientUtil;
import com.crawl.util.SimpleLogger;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by yangwang on 16-8-19.
 */
public abstract class HttpClient {
    private Logger logger = SimpleLogger.getSimpleLogger(HttpClient.class);
    public InputStream getWebPageInputStream(String url){
        try {
            CloseableHttpResponse response = HttpClientUtil.getResponse(url);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Page getWebPage(String url){
        Page page = new Page();
        CloseableHttpResponse response = null;
        try {
            response = HttpClientUtil.getResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(url);
        try {
            if(page.getStatusCode() == 200){
                page.setHtml(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            HttpClientUtil.setCookieStore(cookieStore);
        } catch (Exception e){
            logger.warn("反序列化Cookie失败,没有找到Cookie文件");
            return false;
        }
        return true;
    }
    public abstract void initHttpClient();
}
