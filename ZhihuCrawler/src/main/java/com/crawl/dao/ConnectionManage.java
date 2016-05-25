package com.crawl.dao;

import com.crawl.util.MyLogger;
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
                //判断是否已有连接
                System.out.println("连接不存在或已被关闭，创建连接...");
                conn = createConnection();
            }
            else{
                System.out.println("连接已存在...");
                return conn;
            }
		} catch (SQLException e) {
			logger.error("SQLException",e);
		}
		return conn;
	}
	public static void close(){
		if(conn != null){
			//System.out.println("关闭连接中");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("SQLException",e);
			}
		}
	}
	private static Connection createConnection(){
		Properties p = new Properties();
		try {
			p.load(MyLogger.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = p.getProperty("host");
		String user = p.getProperty("username");
		String password = p.getProperty("password");
		String url="jdbc:mysql://" + host + ":3306/zhihu";
		Connection con=null;
		try{
			Class.forName("org.gjt.mm.mysql.Driver") ;//加载驱动
			con = DriverManager.getConnection(url,user,password);//建立mysql的连接
			//System.out.println("success!");
		}catch(ClassNotFoundException e1){
			System.out.println("数据库驱动不存在！");
			e1.printStackTrace();
			logger.error("ClassNotFoundException",e1);
		}
		catch(SQLException e2){
			System.out.println("数据库存在异常！");
			e2.printStackTrace();
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