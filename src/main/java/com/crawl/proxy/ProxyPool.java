package com.crawl.proxy;

import com.crawl.proxy.entity.Proxy;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;

/**
 * 代理池
 */
public class ProxyPool {
    public final static Set<String> proxySet = new HashSet<String>();
    /**
     * 代理池队列
     */
    public final static DelayQueue<Proxy> proxyQueue = new DelayQueue();

}
