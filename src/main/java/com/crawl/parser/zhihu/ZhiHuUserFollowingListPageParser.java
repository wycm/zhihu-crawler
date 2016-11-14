package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.parser.ListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwang on 16-8-24.
 * 知乎“我关注的人”列表页面
 */
public class ZhiHuUserFollowingListPageParser extends ListPageParser{
    private static ZhiHuUserFollowingListPageParser zhiHuUserFollowingListPageParser;
    public static ZhiHuUserFollowingListPageParser getInstance(){
        if(zhiHuUserFollowingListPageParser == null){
            zhiHuUserFollowingListPageParser = new ZhiHuUserFollowingListPageParser();
        }
        return zhiHuUserFollowingListPageParser;
    }
    @Override
    public List<String> parse(Page page) {
        List<String> list = new ArrayList<String>(20);
        Document doc = Jsoup.parse(page.getHtml());
        Elements es = doc.select(".zm-list-content-medium .zm-list-content-title a");
        for(Element temp:es){
            String userIndex = temp.attr("href") + "/followees";
            list.add(userIndex);
        }
        return list;
    }
}
