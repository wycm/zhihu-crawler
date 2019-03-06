package com.github.wycm.zhihu.service.sender;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.zhihu.ZhihuConstants;
import com.github.wycm.zhihu.dao.mongodb.ZhihuTopicRepository;
import com.github.wycm.zhihu.dao.mongodb.entity.Topic;
import com.github.wycm.zhihu.task.ZhihuTopicPageTask;
import com.github.wycm.zhihu.task.ZhihuUserTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class TopicPageTaskSender extends BaseSender {
    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private ZhihuTopicRepository zhihuTopicRepository;

    @Autowired
    private RedisLockUtil redisLockUtil;

    private volatile int counter = 0;

    //每天01:01:10时执行，抓取topic
//    @Scheduled(cron = "10 40 01 * * ?")
//    @Scheduled(initialDelay = 5000, fixedDelay = 1000 * 60 * 60)
    @Override
    public void send() {
        new Thread(() -> {
            log.info("start send TopicPageTask message");
            counter = counter + 1;
            List<Topic> list = zhihuTopicRepository.findAll();
            if (list.size() < 10000 || counter % 24 == 0){
                if (!CollectionUtils.isEmpty(list)) {
                    list.forEach(t -> {
                        String requestId = UUID.randomUUID().toString();
                        if (redisLockUtil.lock(ZhihuTopicPageTask.class.getSimpleName() + "LockKey:" +
                                        ZhihuConstants.ZHIHU_ROOT_TOPIC_CHILDREN_URL_TEMPLATE.replace("${topicId}", t.getId()),
                                requestId, 1000 * 3600 * 23)) {
                            taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(ZhihuTopicPageTask.class),
                                    new CrawlerMessage(ZhihuConstants.ZHIHU_ROOT_TOPIC_CHILDREN_URL_TEMPLATE.replace("${topicId}", t.getId())), 100000);
                        }
                    });
                } else {
                    String requestId = UUID.randomUUID().toString();
                    if (redisLockUtil.lock(ZhihuTopicPageTask.class.getSimpleName() + "LockKey:" +
                                    ZhihuConstants.ZHIHU_ROOT_TOPIC_CHILDREN_URL_TEMPLATE.replace("${topicId}", "19776749"),
                            requestId, 1000 * 3600 * 23)) {
                        taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(ZhihuTopicPageTask.class),
                                new CrawlerMessage(ZhihuConstants.ZHIHU_ROOT_TOPIC_CHILDREN_URL_TEMPLATE.replace("${topicId}", "19776749")), 100000);
                    }
                }
            }
            log.info("end send TopicPageTask message");
        }).start();
    }
}
