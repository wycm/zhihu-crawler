package com.crawl.dao;

import com.crawl.entity.User;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 *
 */
public class ZhihuDAO {
    private static Logger logger = MyLogger.getMyLogger(ZhihuDAO.class);

    /**
     * 数据库表初始化，创建数据库表。
     * 如果存在的话，则不创建
     * @param cn
     */
    public static void DBTablesInit(Connection cn){
        ResultSet rs = null;
        Properties p = new Properties();
        try {
            //加载properties文件
            p.load(ZhihuDAO.class.getResourceAsStream("/jdbc.properties"));
            rs = cn.getMetaData().getTables(null, null, "href", null);
            Statement st = cn.createStatement();
            //不存在href表
            if(!rs.next()){
                //创建href表
                st.execute(p.getProperty("createHrefTable"));
                logger.info("href表创建成功");
            }
            else{
                logger.info("href表已存在");
            }
            rs = cn.getMetaData().getTables(null, null, "user", null);
            //不存在user表
            if(!rs.next()){
                //创建user表
                st.execute(p.getProperty("createUserTable"));
                logger.info("user表创建成功");
            }
            else{
                logger.info("user表已存在");
            }
            rs.close();
            st.close();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断该数据库中是否存在该用户
     * @param cn
     * @param sql 判断该sql数据库中是否存在
     * @return
     */
    public synchronized static boolean isContain(Connection cn, String sql) throws SQLException {
        int num;
        PreparedStatement pstmt;
        pstmt = cn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            num = rs.getInt("count(*)");
            if(num == 0){
                return false;
            }else{
                return true;
            }
        }
        rs.close();
        pstmt.close();
        return true;
    }
    /**
     * 将用户插入数据库
     * @param cn
     * @param u
     * @throws SQLException
     */
    public synchronized static boolean insetToDB(Connection cn,User u) throws SQLException {
        String isContainSql = "select count(*) from user WHERE url='" + u.getUrl() + "'";
        if(isContain(cn,isContainSql)){
//			cn.close();
            logger.info("数据库已经存在该用户---" + u.getUrl() + "---当前已获取用户数量为:");
            return false;
        }
        String colum = "location,business,sex,employment,username,url,agrees,thanks,asks," +
                "answers,posts,followees,followers,hashId,education";
        String values = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        String sql = "insert into user (" + colum + ") values(" +values+")";
        PreparedStatement pstmt;
        pstmt = cn.prepareStatement(sql);
        pstmt.setString(1,u.getLocation());
        pstmt.setString(2,u.getBusiness());
        pstmt.setString(3,u.getSex());
        pstmt.setString(4,u.getEmployment());
        pstmt.setString(5,u.getUsername());
        pstmt.setString(6,u.getUrl());
        pstmt.setInt(7,u.getAgrees());
        pstmt.setInt(8,u.getThanks());
        pstmt.setInt(9,u.getAsks());
        pstmt.setInt(10,u.getAnswers());
        pstmt.setInt(11,u.getPosts());
        pstmt.setInt(12,u.getFollowees());
        pstmt.setInt(13,u.getFollowers());
        pstmt.setString(14,u.getHashId());
        pstmt.setString(15,u.getEducation());
        pstmt.executeUpdate();
        pstmt.close();
//		cn.close();
        u = null;
        return true;
    }

    /**
     * 将访问过的链接插入数据库
     * @param cn 数据库连接
     * @param md5Href 经过md5处理后的链接
     * @return
     * @throws SQLException
     */
    public synchronized static boolean insertHref(Connection cn,String md5Href) throws SQLException {
        String isContainSql = "select count(*) from href WHERE href='" + md5Href + "'";
        if(isContain(cn,isContainSql)){
            logger.info("数据库已经存在该链接---" + md5Href);
            return false;
        }
        String sql = "insert into href (href) values( ?)";
        PreparedStatement pstmt;
        pstmt = cn.prepareStatement(sql);
        pstmt.setString(1,md5Href);
        pstmt.executeUpdate();
        pstmt.close();
        logger.info("链接插入成功---");
        return true;
    }

    /**
     * 清空表
     * @param cn
     * @throws SQLException
     */
    public synchronized static void deleteHrefTable(Connection cn){
        String sql = "DELETE FROM href";
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
            logger.info("href表删除成功---");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
