package com.github.wycm.zhihu.task;

import com.github.wycm.common.*;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.PatternUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.zhihu.dao.mongodb.entity.Answer;
import com.github.wycm.zhihu.dao.mongodb.entity.Article;
import com.github.wycm.zhihu.dao.mongodb.entity.TopicActivity;
import com.github.wycm.zhihu.service.ZhihuComponent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by wycm on 2018/10/17.
 * 话题动态抓取task
 */
@Slf4j
@NoArgsConstructor
public class TopicActivityPageTask extends AbstractPageTask {
    private ZhihuComponent zhihuComponent;


    public TopicActivityPageTask(CrawlerMessage message, ZhihuComponent zhihuComponent){
        super(message);
        this.url = message.getUrl();
        this.currentRetryTimes = message.getCurrentRetryTimes();
        this.proxyFlag = true;
        this.zhihuComponent = zhihuComponent;
    }

    @Override
    public void run(){
        try {
            Page page = getHttpClient().executeRequestWithRetry(new CrawlerMessage(url), (String s) -> false);
            if (page == null){
                log.debug("executeRequestWithRetry failed, url:{}", url);
            } else {
                handle(page);
            }
            receiveNewTask();
        } catch (InterruptedException e){
            log.error(e.getMessage(), e);
        } catch (Exception e1){
            log.error("meet unknown exception");
            log.error(e1.getMessage(), e1);
            try {
                receiveNewTask();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {

    }

    /**
     * 话题动态页面，可以获取到【问题】、【答案】(问题的部分答案)、【文章】
     * @param page
     */
    @Override
    protected void handle(Page page) {
        if (!page.getHtml().contains("paging")) {
            //代理异常，未能正确返回目标请求数据，丢弃
            currentProxy = null;
            return;
        }
        List<TopicActivity> list = zhihuComponent.getZhihuTopicActivityMongoPageParser().parseListPage(page);
        log.info("topicActivity parse_success, size:{}", list.size());
        list.forEach(t -> {
            zhihuComponent.getZhihuTopicActivityMongodbDao().replace(t);
            if (t.getTarget().getType().equals("answer")) {
                log.debug("find answer activity");
                Answer answer = new Answer();
                zhihuComponent.getZhihuQuestionMongodbDao().insert(t.getTarget().getQuestion());
                BeanUtils.copyProperties(t.getTarget(), answer);
                zhihuComponent.getZhihuAnswerMongodbDao().replace(answer);
            } else if (t.getTarget().getType().equals("article")) {
                log.debug("find article activity");
                Article article = new Article();
                BeanUtils.copyProperties(t.getTarget(), article);
                zhihuComponent.getZhihuArticleMongodbDao().replace(article);
            }
        });
        String topicId = PatternUtil.group(page.getUrl(), "topics/(\\d+)/feeds", 1);
        zhihuComponent.getTopicMongodbDao().updateTopicActivityUpdateTime(topicId);
        log.info("topicActivity store_success, size:{}", list.size());
    }


    @Override
    public void retry() {

        if (getCurrentRetryTimes() <= getMaxRetryTimes()) {
            crawlerMessage.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes() + 1);
            zhihuComponent.getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(this.getClass()), crawlerMessage, 10000);
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
        return 0;
    }
}
