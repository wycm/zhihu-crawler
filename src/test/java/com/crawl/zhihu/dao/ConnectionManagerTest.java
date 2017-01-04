package com.crawl.zhihu.dao;

import com.crawl.core.db.ConnectionManager;
import org.junit.Test;

public class ConnectionManagerTest {
    @Test
    public void testCreateConnection(){
        ConnectionManager.getConnection();
        ConnectionManager.close();
    }
}
