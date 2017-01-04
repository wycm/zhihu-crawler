package com.crawl.proxy;

import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.proxy.site.ip181.Ip181ProxyListPageParser;
import com.crawl.proxy.site.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;

/**
 * 代理池
 */
public class ProxyPool {
    public final static Set<String> proxySet = new HashSet<String>();
    /**
     * 代理池延迟队列
     */
    public final static DelayQueue<Proxy> proxyQueue = new DelayQueue();

    public final static Map<String, Class> proxyMap = new HashMap<>();
    static {
        proxyMap.put("http://www.xicidaili.com/wt", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nn", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wn", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.ip181.com/daili/1.html", Ip181ProxyListPageParser.class);
        proxyQueue.add(new Direct(5000));
    }

}
