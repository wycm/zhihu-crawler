package com.github.wycm.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wycm on 2018/10/24.
 */
@Data
public class CrawlerMessage {
    private String url;
    private int currentRetryTimes = 0;
    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    private Map<String, String> headers = new HashMap<>();
    /**
     * 消息上下文
     */
    private Map<String, String> messageContext = new HashMap<>();
    private String sendTime;

    public CrawlerMessage(){

    }

    public CrawlerMessage(String url) {
        this.url = url;
    }

    public CrawlerMessage(String url, int currentRetryTimes) {
        this.url = url;
        this.currentRetryTimes = currentRetryTimes;
    }

    public CrawlerMessage(String url, String userAgent) {
        this.url = url;
        this.userAgent = userAgent;
    }

    public CrawlerMessage(String url, int currentRetryTimes, String userAgent) {
        this.url = url;
        this.currentRetryTimes = currentRetryTimes;
        this.userAgent = userAgent;
    }
}
