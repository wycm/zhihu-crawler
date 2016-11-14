package com.crawl.dao;

import com.crawl.config.Config;
import com.crawl.util.MyLogger;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DB Connection管理
 */
public class ConnectionManage{
	private static Logger logger = MyLogger.getMyLogger(ConnectionManage.class);
	private static Connection conn;
	public static Connection getConnection(){
		//获取数据库连接
		try {
			if(conn == null || conn.isClosed()){
                conn = createConnection();
            }
            else{
                return conn;
            }
		} catch (SQLException e) {
			logger.error("SQLException",e);
		}
		return conn;
	}
	public static void close(){
		if(conn != null){
			//logger.info("关闭连接中");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("SQLException",e);
			}
		}
	}
	private static Connection createConnection(){
		String host = Config.dbHost;
		String user = Config.dbUsername;
		String password = Config.dbPassword;
		String dbName = Config.dbName;
		String url="jdbc:mysql://" + host + ":3306/" + dbName;
		Connection con=null;
		try{
			Class.forName("org.gjt.mm.mysql.Driver") ;//加载驱动
			con = DriverManager.getConnection(url,user,password);//建立mysql的连接
			logger.info("success!");
		}
		catch(MySQLSyntaxErrorException e){
			logger.error("数据库不存在..请先手动创建创建数据库:" + dbName);
			e.printStackTrace();
		}
		catch(ClassNotFoundException e1){
			logger.error("ClassNotFoundException",e1);
		}
		catch(SQLException e2){
			logger.error("SQLException",e2);
		}
		return con;
	}
	public static void main(String [] args) throws Exception{
		getConnection();
		getConnection();
		close();
	}
}