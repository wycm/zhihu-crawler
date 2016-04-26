package com.crawl.zhihu.crawlimganswer;

import com.crawl.util.MyLogger;
import com.crawl.zhihu.client.Href;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2016/4/14 0014.
 */
public class ParseImgAnswerTask implements Runnable{
    private static Logger logger = MyLogger.getMyLogger(MyThreadPoolExecutor.class);
    private ZhihuHttpClient zhClient;
    ImgAnswerStorage storage = null;
    ThreadPoolExecutor gwpThreadPool = null;//获取网页线程池
    MyThreadPoolExecutor piaThreadPool = null;//解析网页线程池
    public ParseImgAnswerTask(){

    }
    public ParseImgAnswerTask(ZhihuHttpClient zhClient,ImgAnswerStorage storage, ThreadPoolExecutor gwpThreadPool, MyThreadPoolExecutor piaThreadPool){
        this.storage = storage;
        this.gwpThreadPool = gwpThreadPool;
        this.piaThreadPool = piaThreadPool;
        this.zhClient = zhClient;
    }
    @Override
    public void run() {
        Href h = storage.pop();
        String ym = h.getYm();//源码
        Document doc = Jsoup.parse(ym);
        String title = doc.select("title").first().text();
        if(title.contains(" 答过的问题 - 知乎")){
            //答案列表页面
            Elements es = doc.select(".zm-item a.question_link");
            for(Element e:es){
                String answerHref = e.attr("href");
                HttpGet getRequest = new HttpGet("https://www.zhihu.com" + answerHref);
                piaThreadPool.execute(new GetWebPageTask(zhClient,getRequest,storage,gwpThreadPool,piaThreadPool));
            }
        }else {
            //具体答案页面
            int size = doc.select(".zm-editable-content.clearfix img").size();
            if(size > 0){
                //该回答有图片
                storage.getResult().add(h);
            }
        }

    }
}
