package com.crawl.zhihu.entity;

/**
 * 知乎用户资料
 */
public class User {
    //用户名
    private String username;
    //user token
    private String userToken;
    //位置
    private String location;
    //行业
    private String business;
    //性别
    private String sex;
    //企业
    private String employment;
    //企业职位
    private String position;
    //教育
    private String education;
    //用户首页url
    private String url;
    //答案赞同数
    private int agrees;
    //感谢数
    private int thanks;
    //提问数
    private int asks;
    //回答数
    private int answers;
    //文章数
    private int posts;
    //关注人数
    private int followees;
    //粉丝数量
    private int followers;
    // hashId 用户唯一标识
    private String hashId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAgrees() {
        return agrees;
    }

    public void setAgrees(int agrees) {
        this.agrees = agrees;
    }

    public int getThanks() {
        return thanks;
    }

    public void setThanks(int thanks) {
        this.thanks = thanks;
    }

    public int getAsks() {
        return asks;
    }

    public void setAsks(int asks) {
        this.asks = asks;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getFollowees() {
        return followees;
    }

    public void setFollowees(int followees) {
        this.followees = followees;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userToken='" + userToken + '\'' +
                ", location='" + location + '\'' +
                ", business='" + business + '\'' +
                ", sex='" + sex + '\'' +
                ", employment='" + employment + '\'' +
                ", position='" + position + '\'' +
                ", education='" + education + '\'' +
                ", url='" + url + '\'' +
                ", agrees=" + agrees +
                ", thanks=" + thanks +
                ", asks=" + asks +
                ", answers=" + answers +
                ", posts=" + posts +
                ", followees=" + followees +
                ", followers=" + followers +
                ", hashId='" + hashId + '\'' +
                '}';
    }
}
