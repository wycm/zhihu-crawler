package com.crawl.dao;

import org.junit.Test;

/**
 * Created by wy on 2016/6/18 0018.
 */
public class ConnectionManagerTest {
    @Test
    public void testCreateConnection(){
        ConnectionManager.getConnection();
        ConnectionManager.close();
    }
}
