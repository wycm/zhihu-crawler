package com.github.wycm.common;

import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/**
 * Created by wycm on 2019-02-01.
 */
@Slf4j
@Service
public class ShutdownService {
    @PreDestroy
    public void destroy() {
        log.info("start close service");
        Constants.stopService = true;
        ThreadPoolUtil.getThreadPoolExecutorMap().forEach((k, v) -> v.shutdown());
        while (!ThreadPoolUtil.getThreadPoolExecutorMap().values().stream().allMatch(v -> v.isTerminated())){
            try {
                Thread.sleep(1000);
                log.info("shutdown service...");
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        log.info("stop service success");
    }
}
