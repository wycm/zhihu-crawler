package com.github.wycm.zhihu.task;

import com.alibaba.fastjson.JSONObject;
import com.github.wycm.common.*;
import com.github.wycm.common.util.PatternUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.zhihu.ZhihuConstants;
import com.github.wycm.zhihu.dao.mongodb.CrawledUrlMongodbDao;
import com.github.wycm.zhihu.dao.mongodb.ZhihuUserMongodbDao;
import com.github.wycm.zhihu.dao.mongodb.ZhihuUserRepository;
import com.github.wycm.zhihu.dao.mongodb.entity.ZhihuUser;
import com.github.wycm.zhihu.parser.ZhihuUserPageParser;
import com.github.wycm.zhihu.service.ZhihuComponent;
import com.github.wycm.zhihu.util.CrawledUrlUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by wycm on 2018/10/16.
 * 知乎用户抓取task
 */
@Slf4j
public class ZhihuUserTask extends AbstractPageTask {

    private ZhihuComponent zhihuComponent;


    public ZhihuUserTask(CrawlerMessage crawlerMessage, ZhihuComponent zhihuComment) {
        super(crawlerMessage);
        this.zhihuComponent = zhihuComment;
    }

    @Override
    protected void handle(Page page) {
        log.debug(page.getHtml());
        JSONObject jsonObject = JSONObject.parseObject(page.getHtml());
        if (!jsonObject.getJSONObject("paging").getBoolean("is_end")) {
            String userToken = PatternUtil.group(page.getUrl(), "/members/(.*?)/followees", 1);
            String limit = PatternUtil.group(jsonObject.getJSONObject("paging").getString("next"), "offset=(.\\d+)", 1);
            String nextUrl = String.format(ZhihuConstants.USER_FOLLOWEES_URL, userToken, Integer.valueOf(limit));
            CrawlerMessage message = new CrawlerMessage(nextUrl);
            if (!getCrawledUrlMongodbDao().exist(CrawledUrlUtil.generateCrawledUrl(message.getUrl()))
                    || getTaskQueueService().queueSize(CrawlerUtils.getTaskQueueName(ZhihuUserTask.class)) < 100) {
                getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(ZhihuUserTask.class), message, 100000);
            }
        }
        List<ZhihuUser> list = getZhihuUserPageParser().parseListPage(page);
        log.info("parse list user size:{}, url:{}", list.size(), page.getUrl());
        list.stream().filter(t -> {
            if(!getZhihuUserRepository().findById(t.getId()).isPresent()){
                return true;
            }
            return false;
        }).forEach(t -> {
            log.info("parse user success:" + t.getName());
            getZhihuUserMongodbDao().replace(t);
        });
        insertCrawledUrl(page.getUrl());
    }

    @Override
    public void retry() {
        if (getCurrentRetryTimes() <= getMaxRetryTimes()) {
            CrawlerMessage crawlerMessage = new CrawlerMessage(url, getCurrentRetryTimes() + 1);
            getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(ZhihuUserTask.class), crawlerMessage, 100000);
        } else {
            log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
        }
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        ZhihuUserTask task = new ZhihuUserTask(crawlerMessage, zhihuComponent);
        task.crawlerMessage = crawlerMessage;
        task.url = crawlerMessage.getUrl();
        task.currentRetryTimes = crawlerMessage.getCurrentRetryTimes();
        task.proxyFlag = true;
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
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

    private CrawledUrlMongodbDao getCrawledUrlMongodbDao(){
        return zhihuComponent.getCrawledUrlMongodbDao();
    }

    private ZhihuUserPageParser getZhihuUserPageParser(){
        return zhihuComponent.getZhihuUserPageParser();
    }

    private ZhihuUserRepository getZhihuUserRepository(){
        return zhihuComponent.getZhihuUserRepository();
    }

    private ZhihuUserMongodbDao getZhihuUserMongodbDao(){
        return zhihuComponent.getZhihuUserMongodbDao();
    }

    private void insertCrawledUrl(String url){
        zhihuComponent.getCrawledUrlMongodbDao().insertIfNotExist(CrawledUrlUtil.generateCrawledUrl(url));
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
