package com.crawl.zhihu.task;


import com.crawl.core.util.Config;
import com.crawl.core.util.Constants;
import com.crawl.core.util.Md5Util;
import com.crawl.zhihu.dao.ZhiHuDAO;
import com.crawl.zhihu.entity.Page;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.List;

import static com.crawl.zhihu.task.ParseTask.isStopDownload;

public class ListPageTask extends PageTask {

    public ListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }

    @Override
    void retry() {
        zhiHuHttpClient.getDetailPageThreadPool().execute(new ListPageTask(request, Config.isProxy));
    }

    @Override
    void handle(Page page) {
        /**
         * "我关注的人"列表页
         */
        List<String> urlTokenList = JsonPath.parse(page.getHtml()).read("$.data..url_token");
        for (String s : urlTokenList){
            if (s == null){
                continue;
            }
            handleUrl(Constants.INDEX_URL + "/people/" + s + "/following");
        }
    }
    private void handleUrl(String url){
        if(!Config.dbEnable){
            zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));
            return ;
        }
        String md5Url = Md5Util.Convert2Md5(url);
        boolean isRepeat = ZhiHuDAO.insertUrl(md5Url);
        if(!isRepeat){
            /**
             * 防止互相等待，导致死锁
             */
            zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));

        }
    }
}
