package com.github.wycm.zhihu.task;

import com.alibaba.fastjson.JSONObject;
import com.github.wycm.common.*;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.zhihu.ZhihuConstants;
import com.github.wycm.zhihu.dao.mongodb.entity.Topic;
import com.github.wycm.zhihu.service.ZhihuComponent;
import com.github.wycm.zhihu.util.CrawledUrlUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by wycm on 2018/10/16.
 * 话题抓取task
 */
@Slf4j
@NoArgsConstructor
public class ZhihuTopicPageTask extends AbstractPageTask {

    private ZhihuComponent zhihuComponent;


    public ZhihuTopicPageTask(CrawlerMessage crawlerMessage, ZhihuComponent zhihuComment) {
        super(crawlerMessage);
        this.zhihuComponent = zhihuComment;
    }

    @Override
    protected void handle(Page page) {
        if (!page.getHtml().contains("paging")) {
            //代理异常，未能正确返回目标请求数据，丢弃
            currentProxy = null;
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(page.getHtml()).getJSONObject("paging");
        if (!jsonObject.getBoolean("is_end")) {
            String nextUrl = jsonObject.getString("next");
            if (!zhihuComponent.getCrawledUrlMongodbDao().exist(CrawledUrlUtil.generateCrawledUrl(nextUrl))) {
                getTaskQueueService().sendTask(ZhihuConstants.TOPIC_PAGE_TASK_QUEUE_NAME, new CrawlerMessage(nextUrl), 100000);
            }
        }
        List<Topic> list = zhihuComponent.getZhihuTopicMongoPageParser().parseListPage(page);
        list.stream().forEach(t -> {
            log.info("parse topic success:" + t.getName());
            zhihuComponent.getTopicMongodbDao().replace(t);
            String newUrl = String.format("https://www.zhihu.com/api/v3/topics/%s/children", t.getId());
            if (!zhihuComponent.getCrawledUrlMongodbDao().exist(CrawledUrlUtil.generateCrawledUrl(newUrl))) {
                getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(this.getClass()), new CrawlerMessage(newUrl), 100000);
            }
        });
        insertCrawledUrl(page.getUrl());
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {

    }

    private void insertCrawledUrl(String url) {
        zhihuComponent.getCrawledUrlMongodbDao().insertIfNotExist(CrawledUrlUtil.generateCrawledUrl(url));
    }


    @Override
    public void retry() {
        if (getCurrentRetryTimes() <= getMaxRetryTimes()) {
            CrawlerMessage crawlerMessage = new CrawlerMessage(url, getCurrentRetryTimes() + 1);
            getTaskQueueService().sendTask(ZhihuConstants.TOPIC_PAGE_TASK_QUEUE_NAME, crawlerMessage, 100000);
        } else {
            log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
        }
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return zhihuComponent.getZhihuHttpClient();
    }

    @Override
    protected String getProxyQueueName() {
        return zhihuComponent.getCommonProperties().getTargetPageProxyQueueName();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return zhihuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return zhihuComponent.getTaskQueueService();
    }

    @Override
    protected LocalIPService getLocalIPService() {
        return zhihuComponent.getLocalIPService();
    }

    @Override
    public int getMaxRetryTimes() {
        return 3;
    }

    @Override
    public int getCurrentRetryTimes() {
        return this.currentRetryTimes;
    }
}
