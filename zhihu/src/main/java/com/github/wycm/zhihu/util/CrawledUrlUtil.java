package com.github.wycm.zhihu.util;


import com.github.wycm.common.util.Md5Util;
import com.github.wycm.zhihu.dao.mongodb.entity.CrawledUrl;

import java.util.Date;


public class CrawledUrlUtil {
    /**
     * 生成抓取url,替换掉链接中的一些变量
     * @param url
     * @return
     */
    public static CrawledUrl generateCrawledUrl(String url){
        url = url.replaceAll("&as=([A-Z]|\\d)+", "")
                .replaceAll("&cp=([A-Z]|\\d)+", "")
                .replaceAll("&_signature=.+", "");
        CrawledUrl crawledUrl = new CrawledUrl();
        crawledUrl.setUrl(url);
        crawledUrl.setUrlMd5(Md5Util.Convert2Md5(url));
        crawledUrl.setCreateTime(new Date());
        crawledUrl.setUpdateTime(new Date());
        return crawledUrl;
    }
}
