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
 * https://www.zhihu.com/people/wo-yan-chen-mo/following
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
        String userId = getUserId(page.getUrl());
        user.setUsername(doc.select("span[class=ProfileHeader-name]").first().text());//用户名
        setUserInfo(doc, user, "location");//位置&行业
        setUserInfo(doc, user, "company");//公司&职位
        setUserInfo(doc, user, "education");//教育
        user.setUrl("https://www.zhihu.com/people/" + userId);//用户主页
        user.setAgrees(getAnswersInfo(page.getHtml(), "获得 ([0-9]+) 次赞同"));//赞同数
        user.setThanks(getAnswersInfo(page.getHtml(), "获得 ([0-9]+) 次感谢"));//感谢数
        user.setFollowees(Integer.valueOf(doc.select("a[href$=following] [class=Profile-followStatusValue]").first().text()));//关注人数
        user.setFollowers(Integer.valueOf(doc.select("a[href$=followers] [class=Profile-followStatusValue]").first().text()));//关注者
        user.setAsks(getAnswersInfo(page.getHtml(), "提问<span class=\"Tabs-meta\">([0-9]+)</span>"));//提问数
        user.setAnswers(getAnswersInfo(page.getHtml(), "回答<span class=\"Tabs-meta\">([0-9]+)</span>"));//回答数
        user.setPosts(getAnswersInfo(page.getHtml(), "文章<span class=\"Tabs-meta\">([0-9]+)</span>"));//文章数
        if(doc.select("[class=Icon Icon--male]").size() > 0){
            user.setSex("male");
        }
        else if (doc.select("[class=Icon Icon--male]").size() > 0){
            user.setSex("female");
        }
        String s = doc.select("[data-state]").first().toString();
        user.setHashId(getHashId(userId, s));
        return user;
    }
    private void setUserInfo(Document doc, User u, String infoName){
        Element element = doc.select("[class=Icon Icon--" + infoName + "]").first();
        if(element == null){
            return ;
        }
        int i = element.parent().siblingIndex();
        Node node = element.parent().parent().childNode(i + 1);
        if(node instanceof TextNode){
            if (infoName.equals("location")){
                u.setLocation(node.toString());
                int childNodeSize = element.parent().parent().childNodeSize();
                if (childNodeSize <= (i + 1 + 2)){
                    return;
                }
                Node businessNode = element.parent().parent().childNode(i + 1 + 2);
                if(businessNode instanceof TextNode){
                    u.setBusiness(businessNode.toString());
                }
            }
            else if(infoName.equals("company")){
                u.setEmployment(node.toString());
                int childNodeSize = element.parent().parent().childNodeSize();
                if (childNodeSize <= (i + 1 + 2)){
                    return;
                }
                Node positionNode = element.parent().parent().childNode(i + 1 + 2);
                if(positionNode instanceof TextNode){
                    u.setPosition(positionNode.toString());
                }
            }
            else if(infoName.equals("education")){
                u.setEducation(node.toString());
            }

        }
    }
    //解析出当前用户的hashId
    private String getHashId(String userId, String dataState){

        Pattern pattern = Pattern.compile("&quot;" + userId + "&quot;.*&quot;id&quot;:&quot;([a-z0-9]{32}).*&quot;isActive&quot;:1");
        Matcher matcher = pattern.matcher(dataState);
//        System.out.println(matcher.start());
        if(matcher.find()){
            String hashId = matcher.group(1);
            return hashId;
        }
        throw new RuntimeException("not find HashId");
    }

    /**
     * 根据url解析出用户id
     * @param url
     * @return
     */
    private String getUserId(String url){
        Pattern pattern = Pattern.compile("https://www.zhihu.com/[a-z]+/(.*)/(following|followees)");
        Matcher matcher = pattern.matcher(url);
        String userId = null;
        if(matcher.find()){
            userId = matcher.group(1);
            return userId;
        }
        throw new RuntimeException("not parse userId");
    }

    /**
     * 解析用户答案相关信息
     * @return
     */
    private int getAnswersInfo(String  html, String patternStr){
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(html);
        if(matcher.find()){
            String s = matcher.group(1);
            return Integer.valueOf(s);
        }
        return 0;
    }
}