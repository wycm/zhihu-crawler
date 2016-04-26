package com.crawl.test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2016/4/20 0020.
 */
public class QueueTest {
    public static void main(String args []){
        LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<String>();
        try {
            lbq.put("1");
            System.out.println("size:" + lbq.size());
            lbq.put("2");
            System.out.println("size:" + lbq.size());
            lbq.put("3");
            lbq.take();
            System.out.println("size:" + lbq.size());
            Thread.sleep(3000);
            System.out.println("size:" + lbq.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
