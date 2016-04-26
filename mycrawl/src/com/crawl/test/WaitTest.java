package com.crawl.test;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class WaitTest extends Thread{
    public static void main(String args []){
        WaitTest wt1 = new WaitTest();
        WaitTest wt2 = new WaitTest();
        wt1.start();
        try {
            WaitTest.class.wait(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wt2.start();
    }
    @Override
    public void run(){
        System.out.println(this.getName());
    }
}
