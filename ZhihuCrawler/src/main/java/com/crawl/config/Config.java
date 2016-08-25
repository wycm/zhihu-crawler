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
     * 爬虫路口
     */
    public static String  startURL;
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
    }

}
