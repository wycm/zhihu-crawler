package com.crawl.test;

/**
 * 优惠券父类
 */
public class YHQ {
    private String name;
    private int minPrice;//最小优惠价格
    YHQ(String name,int price){
        this.name = name;
        this.minPrice = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPrice() {
        return minPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.minPrice = maxPrice;
    }
}
