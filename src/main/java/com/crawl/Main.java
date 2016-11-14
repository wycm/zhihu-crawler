package com.crawl;

import com.crawl.config.Config;
import com.crawl.zhihu.ZhiHuHttpClient;

/**
 * Created by Administrator on 2016/8/23 0023.
 * 爬虫入口
 */
public class Main {

    public static void main(String args []){
        String startURL = Config.startURL;
        ZhiHuHttpClient.getInstance().startCrawl(startURL);
    }
}
