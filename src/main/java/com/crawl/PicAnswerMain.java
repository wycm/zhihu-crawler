package com.crawl;


import com.crawl.proxy.ProxyHttpClient;
import com.crawl.zhihu.ZhiHuHttpClient;

public class PicAnswerMain {
    public static void main(String[] args){
        ProxyHttpClient.getInstance().startCrawl();
        ZhiHuHttpClient.getInstance().startCrawlAnswerPic("tang-qi-55-37");
    }
}
