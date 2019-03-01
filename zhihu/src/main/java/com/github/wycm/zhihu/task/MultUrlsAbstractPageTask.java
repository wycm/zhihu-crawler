package com.github.wycm.zhihu.task;

import com.github.wycm.common.CrawlerMessage;
import com.github.wycm.common.Page;
import com.github.wycm.proxy.AbstractPageTask;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;


@Slf4j
public abstract class MultUrlsAbstractPageTask extends AbstractPageTask {
    public MultUrlsAbstractPageTask(CrawlerMessage crawlerMessage){
        super(crawlerMessage);
    }
    @Override
    public void run(){
        try {
            Page page = getHttpClient().executeRequestWithRetry(crawlerMessage, jsonPageBannedFunction());
            if (page == null){
                log.error("executeRequestWithRetry failed, url:{}", url);
                handleCrawlFailed();
            } else {
                handle(page);
            }
            receiveNewTask();
        } catch (InterruptedException e){
            log.error(e.getMessage(), e);
        } catch (Exception e1){
            log.error(e1.getMessage(), e1);
            try {
                receiveNewTask();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }
    }

    public abstract Function<String, Boolean> jsonPageBannedFunction();

    public void handleCrawlFailed(){

    }
}
