package com.crawl.zhihu.dao;

import org.junit.Test;

public class ConnectionManagerTest {
    @Test
    public void testCreateConnection(){
        ConnectionManager.getConnection();
        ConnectionManager.close();
    }
}
