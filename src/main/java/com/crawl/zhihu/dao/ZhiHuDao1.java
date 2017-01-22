package com.crawl.zhihu.dao;


import com.crawl.core.dao.ConnectionManager;
import com.crawl.zhihu.entity.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface ZhiHuDao1 {
    public void DBTablesInit();
    boolean isExistRecord(String sql) throws SQLException;
    boolean isExistRecord(Connection cn, String sql) throws SQLException;
    boolean isExistUser(String userToken);
    boolean isExistUser(Connection cn, String userToken);
    boolean insertUser(User user);
    boolean insertUser(Connection cn, User user);
}
