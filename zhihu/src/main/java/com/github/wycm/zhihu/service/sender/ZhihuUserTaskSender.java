package com.github.wycm.zhihu.service.sender;

import com.alibaba.fastjson.JSON;
import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.zhihu.ZhihuConstants;
import com.github.wycm.zhihu.dao.mongodb.ZhihuUserMongodbDao;
import com.github.wycm.zhihu.dao.mongodb.entity.ZhihuUser;
import com.github.wycm.zhihu.task.ZhihuUserTask;
import com.github.wycm.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ZhihuUserTaskSender extends BaseSender {
    public final static int USER_TASK_DELAY = 1000 * 60 * 10;
    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private ZhihuUserMongodbDao zhihuUserMongodbDao;

    //每天01:01:10时执行，抓取
    @Scheduled(initialDelay = 5000, fixedDelay = USER_TASK_DELAY)
    @Override
    public void send() {
        new Thread(() -> {
            log.info("start send ZhihuUser message");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            List<ZhihuUser> list = zhihuUserMongodbDao.findEarliestZhihuUser("updateTime", calendar.getTime(), 10000);
            log.info("ZhihuUserTask list size:{}", list.size());
            AtomicInteger sendSize = new AtomicInteger();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(t -> {
                    String token = t.getUrl_token();
                    String startUrl = String.format(ZhihuConstants.USER_FOLLOWEES_URL, token, 0);
                    CrawlerMessage crawlerMessage = new CrawlerMessage(startUrl);
                    String requestId = UUID.randomUUID().toString();
                    if (redisLockUtil.lock(CrawlerUtils.getLockKeyPrefix(ZhihuUserTask.class) +
                                    token,
                            requestId, USER_TASK_DELAY - (1000 * 60 * 2))) {
                        taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(ZhihuUserTask.class),
                                crawlerMessage, 100000);
                        log.debug("ZhihuUserTask message send success:{}", JSON.toJSONString(crawlerMessage));
                        sendSize.getAndIncrement();
                    }
                });

            } else {
                String startUrl = String.format(ZhihuConstants.USER_FOLLOWEES_URL, "wo-yan-chen-mo", 0);
                String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
                taskQueueService.sendTask(CrawlerUtils.getTaskQueueName(ZhihuUserTask.class), new CrawlerMessage(startUrl, userAgent), 100000);
            }
            log.info("end send ZhihuUser message, sendSize:{}", sendSize.get());
        }).start();

    }
}
