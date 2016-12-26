package com.crawl.zhihu;

import com.crawl.util.HttpClientUtil;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Test;

/**
 * Created by yang.wang on 12/26/16.
 */
public class ModelLoginTest {
    public static void main(String[] args){
        for (int i = 0; i < 10; i++){
            new ModelLogin().login("...", "...");
            HttpClientUtil.setCookieStore(new BasicCookieStore());
        }
    }

}
