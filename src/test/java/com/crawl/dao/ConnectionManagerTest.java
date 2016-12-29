package com.crawl.dao;

import org.junit.Test;

public class ConnectionManagerTest {
    @Test
    public void testCreateConnection(){
        ConnectionManager.getConnection();
        ConnectionManager.close();
    }
}
