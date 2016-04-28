package com.crawl.entity;

/**
 * url
 */
public class Href {
    private String ym;//源码
    private String href;//地址
    private String title;//标题
    public Href(){

    }
    public Href(String ym, String href,String title) {
        this.ym = ym;
        this.href = href;
        this.title = title;
    }

    public String getYm() {
        return ym;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
