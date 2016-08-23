package com.crawl.zhihu;

import com.crawl.config.Config;
import com.crawl.httpclient.HttpClient;
import com.crawl.util.HttpClientUtil;
import org.apache.http.client.CookieStore;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class ZhiHuHttpClient extends HttpClient{
    Logger logger = Logger.getLogger(ZhiHuHttpClient.class);
    private static ZhiHuHttpClient zhiHuHttpClient;
    public ZhiHuHttpClient() {
        initHttpClient();
    }

    public static ZhiHuHttpClient getInstance(){
        if(zhiHuHttpClient == null){
            zhiHuHttpClient = new ZhiHuHttpClient();
        }
        return zhiHuHttpClient;
    }
    /**
     * 初始化知乎客户端
     * 模拟登录知乎，持久化Cookie到本地
     * 不用以后每次都登录
     */
    @Override
    public void initHttpClient() {
        Properties properties = new Properties();
        if(!antiSerializeCookieStore("/zhihucookies1")){
            new ModelLogin().login(this, Config.emailOrPhoneNum, Config.password);
        }
    }

}
