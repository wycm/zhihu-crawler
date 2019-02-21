package com.github.wycm.zhihu.task;

import com.alibaba.fastjson.JSON;
import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.proxy.AbstractHttpClient;
import com.github.wycm.proxy.AbstractPageTask;
import com.github.wycm.proxy.ProxyListPageParser;
import com.github.wycm.proxy.ProxyPageProxyPool;
import com.github.wycm.proxy.site.ProxyListPageParserFactory;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.zhihu.ZhihuConstants;
import com.github.wycm.zhihu.service.ZhihuComponent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;


/**
 * 下载代理网页并解析
 * 若下载失败，通过代理去下载代理网页
 */
@Slf4j
@NoArgsConstructor
public class ZhihuProxyPageDownloadTask extends AbstractPageTask implements Runnable, RetryHandler {
    private ZhihuComponent zhihuComponent;


    public ZhihuProxyPageDownloadTask(CrawlerMessage crawlerMessage, boolean proxyFlag, ZhihuComponent zhihuComponent) {
        this.crawlerMessage = crawlerMessage;
        this.url = crawlerMessage.getUrl();
        this.proxyFlag = proxyFlag;
        this.currentRetryTimes = crawlerMessage.getCurrentRetryTimes();
        this.zhihuComponent = zhihuComponent;
    }


    @Override
    protected AbstractHttpClient getHttpClient() {
        return zhihuComponent.getProxyHttpClient();
    }

    @Override
    protected String getProxyQueueName() {
        return zhihuComponent.getCommonProperties().getProxyPageProxyQueueName();
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
    protected LocalIPService getLocalIPService() {
        return zhihuComponent.getLocalIPService();
    }

    @Override
    public int getMaxRetryTimes() {
        return 3;
    }

    @Override
    public int getCurrentRetryTimes() {
        return this.currentRetryTimes;
    }

    @Override
    public void handle(Page page) {
        if (page.getHtml() == null || page.getHtml().equals("")) {
            return;
        }

        ProxyListPageParser parser = ProxyListPageParserFactory.
                getProxyListPageParser(getProxyPageProxyPool().proxyMap.get(url));
        List<Proxy> proxyList = parser.parse(page.getHtml());
        for (Proxy p : proxyList) {
            String proxyPagekey = ZhihuConstants.PROXY_PAGE_REDIS_KEY_PREFIX + p.getProxyStr();
            Jedis jedis = getJedisPool().getResource();
            boolean containFlag;
            try {
                containFlag = jedis.exists(proxyPagekey);
            } finally {
                jedis.close();
            }
            if (!containFlag) {
                String requestId = UUID.randomUUID().toString();
                if (getRedisLockUtil().lock(proxyPagekey, requestId, Constants.REDIS_TIMEOUT * 1000)) {
                    getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(ZhihuProxyPageProxyTestTask.class), JSON.toJSONString(p), 100000);
                }
            }
            String zhihuPageKey = ZhihuConstants.ZHIHU_PAGE_REDIS_KEY_PREFIX + p.getProxyStr();
            Proxy copyProxy = new Proxy();
            BeanUtils.copyProperties(p, copyProxy);
            p = copyProxy;
            jedis = getJedisPool().getResource();
            try {
                containFlag = jedis.exists(zhihuPageKey);
            } finally {
                jedis.close();
            }
            if (!containFlag) {
                String requestId = UUID.randomUUID().toString();
                if (getRedisLockUtil().lock(zhihuPageKey, requestId, Constants.REDIS_TIMEOUT * 1000)) {
                    getTaskQueueService().sendTask(CrawlerUtils.getTaskQueueName(ZhihuPageProxyTestTask.class), JSON.toJSONString(p), 100000);
                }
            }
        }
    }

    @Override
    protected void createNewTask(CrawlerMessage crawlerMessage) {
        if (Constants.stopService){
            return;
        }
        ZhihuProxyPageDownloadTask task = new ZhihuProxyPageDownloadTask(crawlerMessage, true, zhihuComponent);
        task.crawlerMessage = crawlerMessage;
        task.url = crawlerMessage.getUrl();
        task.currentRetryTimes = crawlerMessage.getCurrentRetryTimes();
        task.proxyFlag = true;
        ThreadPoolUtil.getThreadPool(this.getClass()).execute(task);
        log.info("create new {} success", this.getClass().getSimpleName());
    }

    private ProxyPageProxyPool getProxyPageProxyPool(){
        return zhihuComponent.getProxyPageProxyPool();
    }

    private JedisPool getJedisPool(){
        return zhihuComponent.getJedisPool();
    }

    private RedisLockUtil getRedisLockUtil(){
        return zhihuComponent.getRedisLockUtil();
    }

    private String getProxyStr(Proxy proxy) {
        if (proxy == null) {
            return "";
        }
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
