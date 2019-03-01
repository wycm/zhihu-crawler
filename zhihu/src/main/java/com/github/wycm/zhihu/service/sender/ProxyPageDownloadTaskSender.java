package com.github.wycm.zhihu.service.sender;

import com.alibaba.fastjson.JSON;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.proxy.ProxyPageProxyPool;
import com.github.wycm.zhihu.task.ZhihuProxyPageDownloadTask;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProxyPageDownloadTaskSender extends BaseSender{
    @Autowired
    TaskQueueService taskQueueService;

    @Autowired
    private ProxyPageProxyPool proxyPageProxyPool;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60)
    @Override
    public void send() {
        log.info("start send ProxyPageDownloadTask message");
        if (taskQueueService.queueSize(CrawlerUtils.getTaskQueueName(ZhihuProxyPageDownloadTask.class)) > 1000){
            log.info(CrawlerUtils.getTaskQueueName(ZhihuProxyPageDownloadTask.class) + "size more than 1000, not send task");
            return;
        }
        new Thread(() -> {
            proxyPageProxyPool.proxyMap.keySet().forEach(url -> {
                String requestId = UUID.randomUUID().toString();
                if (redisLockUtil.lock(CrawlerUtils.getLockKeyPrefix(ZhihuProxyPageDownloadTask.class) + url
                        , requestId
                        , 1000 * 60 * 50)){
                    CrawlerMessage crawlerMessage = new CrawlerMessage(url);
                    taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(ZhihuProxyPageDownloadTask.class), crawlerMessage, 100000);
                    log.info("ProxyPageDownloadTask message send success:{}", JSON.toJSONString(crawlerMessage));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.warn("get lock failed : {}", CrawlerUtils.getLockKeyPrefix(ZhihuProxyPageDownloadTask.class) + url);
                }
            });
            log.info("end send ProxyPageDownloadTask message");
        }).start();

    }
}
