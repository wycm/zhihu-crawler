package com.github.wycm.zhihu;

import org.junit.Test;

public class ExceptionTest {
    @Test
    public void test(){
        try {
            Thread.sleep(1000);
            throw new InterruptedException();
        } catch (InterruptedException e) {
            System.out.println(1);
            e.printStackTrace();
        } catch (Exception e1){
            System.out.println(2);
            e1.printStackTrace();
        }
    }
}
