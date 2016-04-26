package com.crawl.test;

/**
 * Created by Administrator on 2016/4/19 0019.
 */
public class Test1 {
    public static void main(String args []){
        System.out.println("12345".getBytes().length);
    }
    public static int getCount(int n){
        if(n == 1){
            return 1;
        }
        if(n == 2){
            return 2;
        }
        if(n == 3){
            return 4;
        }
        return getCount(n - 1) + getCount(n - 2) + getCount(n - 3);
    }
    public static int getCount1(int n){
        if(n == 1){
            return 1;
        }
        if(n == 2){
            return 2;
        }
        if(n == 3){
            return 4;
        }
        int f1 = 1;
        int f2 = 2;
        int f3 = 4;
        int result = 0;
        for(int i = 4;i <= n;i++){
            result = f1 + f2 + f3;
            f1 = f2;
            f2 = f3;
            f3 = result;
        }
        return result;
    }
}
