package com.crawl.dao;

import org.junit.Test;

import java.sql.Connection;

/**
 * Created by wy on 2016/6/18 0018.
 */
public class ZhihuDAOTest {
    @Test
    public void testDBTablesInit(){
        Connection cn = ConnectionManager.getConnection();
        ZhiHuDAO.DBTablesInit(cn);
    }
}
