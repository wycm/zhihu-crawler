package com.github.wycm.zhihu.service.receiver;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.zhihu.task.ZhihuTopicPageTask;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
@Slf4j
@NoArgsConstructor
public class TopicPageTaskReceiver extends BaseReceiver{

//    @PostConstruct
    @Override
    public void receive() {
        new Thread(() -> receive(ZhihuTopicPageTask.class)).start();
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        ZhihuTopicPageTask task = new ZhihuTopicPageTask(crawlerMessage, zhihuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
