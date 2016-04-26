package com.crawl.test;

import java.util.Arrays;

/**
 * BubbleSort
 */
public class BubbleSort {
    public static void main(String args []){
        int[] a = {2,3,5,7,9,12,56,6};
        for(int i = 0;i < a.length;i++){
            for(int j = 1;j < a.length - i;j++){
                if(a[j - 1] > a[j]){
                    int temp = a[j-1];
                    a[j - 1] = a[j];
                    a[j] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(a));
    }
}
