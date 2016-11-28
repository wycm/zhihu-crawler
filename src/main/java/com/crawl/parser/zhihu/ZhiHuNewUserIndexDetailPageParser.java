package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wy on 11/28/2016.
 * https://www.zhihu.com/people/wo-yan-chen-mo/followees
 * 新版followess页面解析出用户详细信息
 */
public class ZhiHuNewUserIndexDetailPageParser extends DetailPageParser{
    private static ZhiHuNewUserIndexDetailPageParser zhiHuNewUserIndexDetailPageParser;
    public static ZhiHuNewUserIndexDetailPageParser getInstance(){
        if(zhiHuNewUserIndexDetailPageParser == null){
            zhiHuNewUserIndexDetailPageParser = new ZhiHuNewUserIndexDetailPageParser();
        }
        return zhiHuNewUserIndexDetailPageParser;
    }
    @Override
    public User parse(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        User user = new User();
        user.setUsername(doc.select("span[class=ProfileHeader-name]").first().text());//用户名
        user.setLocation(getUserInfo(doc, "location"));//位置
        user.setEmployment(getUserInfo(doc, "company"));//公司
        user.setEducation(getUserInfo(doc, "education"));//教育
        // TODO: 11/28/2016
        String s = doc.select("[data-state]").first().toString();
        user.setHashId(getHashId(s));
        return user;
    }
    private String getUserInfo(Document doc, String infoName){
        Element element = doc.select("[class=Icon Icon--" + infoName + "]").first();
        if(element == null){
            return "";
        }
        int i = element.parent().siblingIndex();
        Node location = element.parent().parent().childNode(i + 1);
        if(location instanceof TextNode){
            return location.toString();
        }
        return "";
    }
    private String getHashId(String dataState){
        Pattern pattern = Pattern.compile(",&quot;id&quot;:&quot;([a-z0-9]{32})&quot;,&quot;");
        Matcher matcher = pattern.matcher(dataState);
        if(matcher.find()){
            String hashId = matcher.group(1);
            return hashId;
        }
        throw new RuntimeException("not find HashId");
    }
}