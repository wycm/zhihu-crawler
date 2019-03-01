package com.github.wycm.zhihu.service.receiver;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.SystemUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.zhihu.service.ZhihuComponent;
import com.github.wycm.zhihu.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseReceiver<T extends AbstractPageTask> {
    @Autowired
    protected TaskQueueService taskQueueService;

    @Autowired
    protected ZhihuComponent zhihuComponent;

    static {
        ThreadPoolUtil.createThreadPool(ZhihuUserTask.class, SystemUtil.getRecommendThreadSize());
        ThreadPoolUtil.createThreadPool(ZhihuTopicPageTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(ZhihuProxyPageProxyTestTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(ZhihuPageProxyTestTask.class, SystemUtil.getRecommendThreadSize() / 2);
        ThreadPoolUtil.createThreadPool(ZhihuProxyPageDownloadTask.class, SystemUtil.getRecommendThreadSize() / 4);
    }


    protected abstract void receive();

    protected void receive(Class<T> tClass){
        Thread.currentThread().setName(tClass.getSimpleName() + "Receiver");
        if ("test".equals(System.getProperties().getProperty("env"))){
            log.info("test env...");
            return;
        }
        log.info("start receive {} message", CrawlerUtils.getTaskQueueName(tClass));
        int corePoolSize = ThreadPoolUtil
                .getThreadPool(tClass).getCorePoolSize();
        for (int i = 0; i < corePoolSize; i++){
            CrawlerMessage message = null;
            try {
                message = taskQueueService.receiveTask(CrawlerUtils.getTaskQueueName(tClass));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
            ThreadPoolUtil
                    .getThreadPool(tClass)
                    .execute(createNewTask(message));
            log.info("create new task success");
        }
    }

    protected abstract Runnable createNewTask(CrawlerMessage crawlerMessage);

}
