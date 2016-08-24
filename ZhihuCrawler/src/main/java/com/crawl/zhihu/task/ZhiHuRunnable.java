package com.crawl.zhihu.task;

import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.ZhiHuTaskManager;

/**
 * Created by yangwang on 16-8-24.
 */
public abstract class ZhiHuRunnable implements Runnable{
    protected ZhiHuTaskManager zhiHuTaskManager;
    protected ZhiHuHttpClient zhiHuHttpClient;
}
