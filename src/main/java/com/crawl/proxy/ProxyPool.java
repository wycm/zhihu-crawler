package com.crawl.proxy;

import com.crawl.proxy.entity.Proxy;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;

public class ProxyPool {
    public final static Set<String> proxySet = new HashSet<String>();
    public final static DelayQueue<Proxy> queue = new DelayQueue();

}
