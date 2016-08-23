package com.crawl.zhihu.client;

import com.crawl.entity.User;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Administrator on 2016/4/9 0009.
 */
public class Result {
    private static Logger logger = MyLogger.getMyLogger(Result.class);
    private Set<String> hrefSet;//存放访问过的链接
    private Vector<User> userVector;//存放解析出用户

    public Result() {
        this.userVector = new Vector<User>();
        this.hrefSet = new HashSet<String>();
        hrefSet=Collections.synchronizedSet(this.hrefSet);//将该Set设置为线程安全的
    }

    public Set<String> getHrefSet() {
        return hrefSet;
    }

    public void setHrefSet(Set<String> hrefSet) {
        this.hrefSet = hrefSet;
    }

    public Vector<User> getUserVector() {
        return userVector;
    }
    public void setUserVector(Vector<User> userVector) {
        this.userVector = userVector;
    }
}
