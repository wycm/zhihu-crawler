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

import static com.crawl.core.util.Constants.TIME_INTERVAL;

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
        proxyMap.put("http://www.xicidaili.com/wt/1.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wt/2.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wt/3.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nn/1.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nn/2.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nn/3.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wn/1.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wn/2.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/wn/3.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nt/1.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nt/2.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.xicidaili.com/nt/3.html", XicidailiProxyListPageParser.class);
        proxyMap.put("http://www.ip181.com/daili/1.html", Ip181ProxyListPageParser.class);
        proxyMap.put("http://www.ip181.com/daili/2.html", Ip181ProxyListPageParser.class);
        proxyMap.put("http://www.ip181.com/daili/3.html", Ip181ProxyListPageParser.class);
        proxyQueue.add(new Direct(TIME_INTERVAL));
    }

}
