package com.crawl.zhihu;

import com.crawl.zhihu.entity.Page;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.SimpleLogger;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

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
    public Page getWebPage(String url) throws IOException {
        return getWebPage(url, "UTF-8");
    }
    public Page getWebPage(String url, String charset) throws IOException {
        Page page = new Page();
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(url);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(url);
        try {
            if(page.getStatusCode() == 200){
                page.setHtml(EntityUtils.toString(response.getEntity(), charset));
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
    public Page getWebPage(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
            response = HttpClientUtil.getResponse(request);
            Page page = new Page();
            page.setStatusCode(response.getStatusLine().getStatusCode());
            page.setHtml(EntityUtils.toString(response.getEntity()));
            page.setUrl(request.getURI().toString());
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
