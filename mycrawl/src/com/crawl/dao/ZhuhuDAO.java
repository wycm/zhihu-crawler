package com.crawl.dao;

import com.crawl.entity.User;
import com.crawl.util.MyLogger;
import com.crawl.zhihu.user.ParseWebPageTask;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wy
 */
public class ZhuhuDAO {
    private static Logger logger = MyLogger.getMyLogger(ZhuhuDAO.class);
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
            logger.info("数据库已经存在该用户---" + u.getUrl() + "---当前已获取用户数量为:" + ParseWebPageTask.userCount);
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
        ParseWebPageTask.userCount++;
        logger.info("插入用户成功---已获取" + ParseWebPageTask.userCount + "用户");
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
