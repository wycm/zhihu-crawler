package com.crawl.zhihu.task;


import com.crawl.core.util.Config;
import com.crawl.core.util.Constants;
import com.crawl.zhihu.dao.ZhiHuDAO;
import com.crawl.zhihu.entity.Page;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.List;

public class ListPageTask extends AbstractPageTask {

    public ListPageTask(HttpRequestBase request, boolean proxyFlag) {
        super(request, proxyFlag);
    }


    @Override
    void retry() {
        zhiHuHttpClient.getListPageThreadPool().execute(new ListPageTask(request, Config.isProxy));
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
            handleUserToken(s);
        }
    }
    private void handleUserToken(String userToken){
        String url = Constants.INDEX_URL + "/people/" + userToken + "/following";
        if(!Config.dbEnable){
            zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));
            return ;
        }
        boolean existUserFlag = ZhiHuDAO.isExistUser(userToken);
        while (zhiHuHttpClient.getDetailPageThreadPool().getQueue().size() > 1000){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!existUserFlag || zhiHuHttpClient.getDetailPageThreadPool().getActiveCount() == 0){
            /**
             * 防止互相等待，导致死锁
             */
            zhiHuHttpClient.getDetailPageThreadPool().execute(new DetailPageTask(url, Config.isProxy));

        }
    }
}
