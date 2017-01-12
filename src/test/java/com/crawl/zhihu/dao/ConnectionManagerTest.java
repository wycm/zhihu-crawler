package com.crawl.zhihu.dao;

import com.crawl.core.db.ConnectionManager;
import org.junit.Test;

public class ConnectionManagerTest {
    @Test
    public void testCreateConnection(){
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 10; i++){
            ConnectionManager.getConnection();
            ConnectionManager.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cost time:" + (endTime - startTime) + "ms");
    }
}
