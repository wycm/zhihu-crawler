package com.crawl;


import com.crawl.proxy.entity.Proxy;

import java.util.HashSet;
import java.util.Set;

public class HashSetTest {
    public static void main(String[] args){
        Set<Proxy> set = new HashSet();
        Proxy proxy = new Proxy("1", 1, 1);
        Proxy proxy1 = new Proxy("1", 1, 1);
        System.out.println(proxy.hashCode());
        System.out.println(proxy1.hashCode());
        System.out.println(new String("test").hashCode());
        System.out.println(new String("test").hashCode());
        set.add(proxy);
        set.add(proxy1);
        System.out.println(set.size());
    }
}
