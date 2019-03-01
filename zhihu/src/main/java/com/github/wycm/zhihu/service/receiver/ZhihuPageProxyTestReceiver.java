package com.github.wycm.zhihu.service.receiver;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.zhihu.task.ZhihuPageProxyTestTask;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@NoArgsConstructor
public class ZhihuPageProxyTestReceiver extends BaseReceiver{


    @PostConstruct
    @Override
    public void receive() {
        if ("test".equals(System.getProperties().getProperty("env"))){
            log.info("test env...");
            return;
        }
        log.info("receive thread name {}", Thread.currentThread().getName());
        new Thread(() -> {
            log.info("start receive {} message", CrawlerUtils.getTaskQueueName(ZhihuPageProxyTestTask.class));
            int corePoolSize = ThreadPoolUtil
                    .getThreadPool(ZhihuPageProxyTestTask.class).getCorePoolSize();
            for (int i = 0; i < corePoolSize; i++){
                Proxy proxy = null;
                try {
                    proxy = taskQueueService.receiveProxyTask(CrawlerUtils.getTaskQueueName(ZhihuPageProxyTestTask.class));
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    return;
                }
                ThreadPoolUtil
                        .getThreadPool(ZhihuPageProxyTestTask.class)
                        .execute(new ZhihuPageProxyTestTask(proxy, "https://www.zhihu.com/", zhihuComponent));
            }
        }).start();
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        return null;
    }
}
