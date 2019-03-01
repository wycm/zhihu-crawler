package com.github.wycm.zhihu.service.receiver;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.zhihu.task.ZhihuUserTask;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@NoArgsConstructor
public class ZhihuUserTaskReceiver extends BaseReceiver{

    @PostConstruct
    @Override
    public void receive() {
        new Thread(() -> receive(ZhihuUserTask.class)).start();
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        ZhihuUserTask task = new ZhihuUserTask(crawlerMessage, zhihuComponent);
        task.setUrl(crawlerMessage.getUrl());
        task.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes());
        task.setProxyFlag(true);
        return task;
    }
}
