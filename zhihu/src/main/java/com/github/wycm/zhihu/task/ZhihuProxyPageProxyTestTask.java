package com.github.wycm.zhihu.task;

import com.github.wycm.common.Proxy;
import com.github.wycm.common.ProxyQueue;
import com.github.wycm.common.TaskQueueService;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageProxyTestTask;
import com.github.wycm.zhihu.service.ZhihuComponent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理检测task
 * 通过访问百度首页，能否正确响应
 * 将可用代理添加到ProxyDelayQueue延时队列中
 */
@Slf4j
@NoArgsConstructor
public class ZhihuProxyPageProxyTestTask extends AbstractPageProxyTestTask {
    private ZhihuComponent zhihuComponent;

    public ZhihuProxyPageProxyTestTask(Proxy proxy, String url, ZhihuComponent zhihuComponent) {
        super(proxy, url);
        this.zhihuComponent = zhihuComponent;
    }

    @Override
    protected String getTestUrl() {
        return "https://www.baidu.com/";
    }

    @Override
    protected String getProxyQueueName() {
        return zhihuComponent.getCommonProperties().getProxyPageProxyQueueName();
    }

    @Override
    protected void createNewTask(Proxy proxy, String url) {
        if (Constants.stopService){
            return;
        }
        ZhihuProxyPageProxyTestTask task = new ZhihuProxyPageProxyTestTask(proxy, url, zhihuComponent);
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return zhihuComponent.getProxyHttpClient();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return zhihuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return zhihuComponent.getTaskQueueService();
    }
}
