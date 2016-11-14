package com.crawl.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/8/23 0023.
 * 加载配置文件
 */
public class Config {
    /**
     * 是否持久化到数据库
     */
    public static boolean dbEnable;
    /**
     * 下载网页线程数
     */
    public static int downloadThreadSize;
    /**
     * 验证码路径
     */
    public static String verificationCodePath;
    /**
     * 知乎注册手机号码或有限
     */
    public static String emailOrPhoneNum;
    /**
     * 知乎密码
     */
    public static String password;
    /**
     * 爬虫入口
     */
    public static String  startURL;
    /**
     * 下载网页数
     */
    public static int downloadPageCount;
    /**
     * db.name
     */
    public static String dbName;
    /**
     * db.username
     */
    public static String dbUsername;
    /**
     * db.host
     */
    public static String dbHost;
    /**
     * db.password
     */
    public static String dbPassword;
    /**
     * 创建href表语句
     */
    public static String createHrefTable;

    /**
     * 创建user表语句
     */
    public static String createUserTable;

    /**
     * cookie路径
     */
    public static String cookiePath;
    static {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbEnable = Boolean.parseBoolean(p.getProperty("db.enable"));
        verificationCodePath = p.getProperty("verificationCodePath");
        emailOrPhoneNum = p.getProperty("zhiHu.emailOrPhoneNum");
        password = p.getProperty("zhiHu.password");
        startURL = p.getProperty("startURL");
        downloadPageCount = Integer.valueOf(p.getProperty("downloadPageCount"));
        downloadThreadSize = Integer.valueOf(p.getProperty("downloadThreadSize"));
        cookiePath = p.getProperty("cookiePath");
        if (dbEnable){
            dbName = p.getProperty("db.name");
            dbHost = p.getProperty("db.host");
            dbUsername = p.getProperty("db.username");
            dbPassword = p.getProperty("db.password");
            createHrefTable = p.getProperty("createHrefTable");
            createUserTable = p.getProperty("createUserTable");
        }
    }

}
