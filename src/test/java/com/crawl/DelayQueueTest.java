package com.crawl;

import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;

import static com.crawl.proxy.ProxyPool.proxyQueue;

public class DelayQueueTest {
    public static void main(String[] args){
        Proxy proxy = new Proxy("123", 123, 1000);
        proxyQueue.add(proxy);
        try {
            proxyQueue.take();
            System.out.println("出队成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
