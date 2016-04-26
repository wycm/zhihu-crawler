package com.crawl.test;

import java.util.List;

public class Test2 {
    /**
     * 满包邮
     */
    static class Mby extends YHQ{
        Mby(String name,int price) {
            super(name,price);
        }
    }

    /**
     *红包
     */
    static class RedEnvelope extends YHQ{
        private int free;//减免金额
        RedEnvelope(String name,int price,int free){
            super(name,price);
            this.free = free;
        }

        public int getFree() {
            return free;
        }

        public void setFree(int free) {
            this.free = free;
        }
    }
    public YHQ getSolution(int allPrice,int courierPrice,Mby m,RedEnvelope re){
        if(courierPrice >= re.getFree()){
            //正常快递费用大于红包减免费用，使用"满包邮"
            return m;
        }
        else{
            //否则使用"红包"
            return re;
        }
    }

    public static void main(String args []){
    }
}
