package com.crawl.entity;

/**
 * Created by yangwang on 16-8-19.
 * 网页
 */
public class Page {
    private String url;
    private int statusCode;//响应状态码
    private String html;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
