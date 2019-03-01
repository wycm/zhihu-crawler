package com.github.wycm.zhihu.service.receiver;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Proxy;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.zhihu.task.ZhihuProxyPageProxyTestTask;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@NoArgsConstructor
public class ProxyPageProxyTestReceiver extends BaseReceiver{
    @Autowired
    private TaskQueueService taskQueueService;

    @PostConstruct
    @Override
    public void receive() {
        if ("test".equals(System.getProperties().getProperty("env"))){
            log.info("test env...");
            return;
        }
        new Thread(() -> {
            log.info("start receive {} message", CrawlerUtils.getTaskQueueName(ZhihuProxyPageProxyTestTask.class));
            int corePoolSize = ThreadPoolUtil
                    .getThreadPool(ZhihuProxyPageProxyTestTask.class).getCorePoolSize();
            for (int i = 0; i < corePoolSize; i++){
                Proxy proxy = null;
                try {
                    proxy = taskQueueService.receiveProxyTask(CrawlerUtils.getTaskQueueName(ZhihuProxyPageProxyTestTask.class));
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    return;
                }
                ThreadPoolUtil
                        .getThreadPool(ZhihuProxyPageProxyTestTask.class)
                        .execute(new ZhihuProxyPageProxyTestTask(proxy, "https://www.zhihu.com", zhihuComponent));
            }
        }).start();
    }

    @Override
    protected Runnable createNewTask(CrawlerMessage crawlerMessage) {
        return null;
    }
}
