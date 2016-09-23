package com.crawl.parser.zhihu;

import com.crawl.entity.Page;
import com.crawl.entity.User;
import com.crawl.parser.DetailPageParser;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * Created by yangwang on 16-8-24.
 * https://www.zhihu.com/people/wo-yan-chen-mo/followees
 * followess页面解析出用户详细信息
 */
public class ZhiHuUserIndexDetailPageParser extends DetailPageParser{
    Logger logger = MyLogger.getLogger(ZhiHuUserIndexDetailPageParser.class);
    private static ZhiHuUserIndexDetailPageParser zhiHuUserIndexDetailPageParser;
    public static ZhiHuUserIndexDetailPageParser getInstance(){
        if(zhiHuUserIndexDetailPageParser == null){
            zhiHuUserIndexDetailPageParser = new ZhiHuUserIndexDetailPageParser();
        }
        return zhiHuUserIndexDetailPageParser;
    }
    @Override
    public User parse(Page page) {
        Document doc = Jsoup.parse(page.getHtml());
        return parseUserdetail(doc);
    }
    /**
     * 解析个人资料
     * @param doc
     * @return
     */
    private User parseUserdetail(Document doc){
        User u = new User();
        u.setLocation(getUserinfo(doc,"location"));//位置
        u.setBusiness(getUserinfo(doc,"business"));//行业
        u.setEmployment(getUserinfo(doc,"employment"));//企业
        u.setPosition(getUserinfo(doc,"position"));//职位
        u.setEducation(getUserinfo(doc,"education"));//教育
        u.setUsername(doc.select(".title-section a").first().text());//用户名
        u.setUrl("https://www.zhihu.com" + doc.select(".title-section a").first().attr("href"));//用户首页链接
        u.setAgrees(Integer.valueOf(doc.select(".zm-profile-header-user-agree strong").first().text()));//赞同数
        u.setThanks(Integer.valueOf(doc.select(".zm-profile-header-user-thanks strong").first().text()));//感谢数
        u.setFollowees(Integer.valueOf(doc.select(".zm-profile-side-following strong").first().text()));//关注人数
        u.setFollowers(Integer.valueOf(doc.select(".zm-profile-side-following strong").get(1).text()));//关注者
        u.setAsks(Integer.valueOf(doc.select("div.profile-navbar a[href$=asks] span").first().text()));//提问数
        u.setAnswers(Integer.valueOf(doc.select("div.profile-navbar a[href$=answers] span").first().text()));//回答数
        u.setPosts(Integer.valueOf(doc.select("div.profile-navbar a[href$=posts] span").first().text()));//文章数
        if(doc.select("div.zm-profile-header-user-describe span.gender i[class*=female]").size() > 0){
            u.setSex("female");
        }
        else if (doc.select("div.zm-profile-header-user-describe span.gender i[class*=male]").size() > 0){
            u.setSex("male");
        }
        try {
            u.setHashId(doc.select(".zm-profile-header-op-btns.clearfix button").first().attr("data-id"));
        }catch (NullPointerException e){
            //解析我的主页时，会出现空指针
            u.setHashId("843df56056dc14b8dd36ace99be09337");
        }
        return u;
    }
    /**
     * 获取用户个人资料
     * @param doc
     * @param infoName
     * @return
     */
    private String getUserinfo(Document doc,String infoName){
        Element e = doc.select(".zm-profile-header-user-describe ." + infoName + ".item").first();
        if(e == null){
            return "";
        } else{
            return e.attr("title");
        }
    }
}
