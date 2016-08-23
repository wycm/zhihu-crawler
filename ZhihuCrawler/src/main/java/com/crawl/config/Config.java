package com.crawl.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/8/23 0023.
 *
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

    static {
        Properties p = new Properties();
        try {
            p.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbEnable = Boolean.parseBoolean(p.getProperty("db.enable"));
        verificationCodePath = p.getProperty("verificationCodePath");
    }

}
