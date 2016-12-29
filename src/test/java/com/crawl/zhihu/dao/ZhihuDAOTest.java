package com.crawl.zhihu.dao;

import org.junit.Test;

import java.sql.Connection;
public class ZhihuDAOTest {
    @Test
    public void testDBTablesInit(){
        Connection cn = ConnectionManager.getConnection();
        ZhiHuDAO.DBTablesInit(cn);
    }
}
