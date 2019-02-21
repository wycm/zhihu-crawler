package com.github.wycm.zhihu.task;

import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageProxyTestTask;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.zhihu.service.ZhihuComponent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理检测task
 * 通过访问头条首页，能否正确响应
 * 将可用代理添加到DelayQueue延时队列中
 */
@Slf4j
@NoArgsConstructor
public class ZhihuPageProxyTestTask extends AbstractPageProxyTestTask {
    private ZhihuComponent zhihuComponent;

    public ZhihuPageProxyTestTask(Proxy proxy, String url, ZhihuComponent zhihuComponent) {
        super(proxy, url);
        this.zhihuComponent = zhihuComponent;
    }

    @Override
    protected String getTestUrl() {
        return "https://www.zhihu.com/";
    }

    @Override
    protected String getProxyQueueName() {
        return zhihuComponent.getCommonProperties().getTargetPageProxyQueueName();
    }

    @Override
    protected AbstractHttpClient getHttpClient() {
        return zhihuComponent.getZhihuHttpClient();
    }

    @Override
    protected ProxyQueue getProxyQueue() {
        return zhihuComponent.getProxyQueue();
    }

    @Override
    protected TaskQueueService getTaskQueueService() {
        return zhihuComponent.getTaskQueueService();
    }
    @Override
    protected void createNewTask(Proxy proxy, String url) {
        if (Constants.stopService){
            return;
        }
        ZhihuPageProxyTestTask task = new ZhihuPageProxyTestTask(proxy, url, zhihuComponent);
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    @Override
    protected boolean responseError(Page page){
        boolean flag = page == null || page.getStatusCode() != 200 || AbstractPageTask.getHtmlPageBannedFunction().apply(page.getHtml());
        if (flag){
            log.warn("zhihu proxy test, ip banned, url:{}, response:{}, discard ip:{}, port:{}", page.getUrl(), page.getHtml(), proxy.getIp(), proxy.getPort());
        }
         return flag;
    }
}
