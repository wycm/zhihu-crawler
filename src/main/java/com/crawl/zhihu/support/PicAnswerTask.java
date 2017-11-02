package com.crawl.zhihu.support;

import com.crawl.core.util.Constants;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Direct;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Answer;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.task.AbstractPageTask;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 图片答案task
 */
public class PicAnswerTask extends AbstractPageTask{
    private static Logger logger = LoggerFactory.getLogger(PicAnswerTask.class);
    private String userToken;

    public PicAnswerTask(){

    }


    public PicAnswerTask(HttpRequestBase request, boolean proxyFlag, String userToken){
        super(request, proxyFlag);
        this.userToken = userToken;
    }

    @Override
    protected void retry() {
        zhiHuHttpClient.getAnswerPageThreadPool().execute(new PicAnswerTask(request, true, this.userToken));
    }

    @Override
    protected void handle(Page page) {
        DocumentContext dc = JsonPath.parse(page.getHtml());
        List<Answer> answerList = parseAnswers(dc);
        for(Answer answer : answerList){
            logger.debug(answer.toString());
            Pattern pattern = Pattern.compile("data-original=\"(https://pic\\d\\.zhimg\\.com.*?jpg)");
            Matcher matcher = pattern.matcher(answer.getContent());
            Set<String> picUrlSet = new HashSet<String>();
            while (matcher.find()){
                String picUrl = matcher.group(1);
                picUrlSet.add(picUrl);
            }

            logger.info("问题:{} ; url: {};",answer.getQuestionTitle(), answer.getUrl());
            Iterator<String> iterator = picUrlSet.iterator();
            while (iterator.hasNext()){
                String url = iterator.next();
                logger.debug("下载图片中...");
                String path = System.getProperty("java.io.tmpdir") + "zhihu/" + answer.getQuestionTitle() + "/";
                String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                HttpClientUtil.downloadFile(url,
                        path,
                        fileName,
                        true);

            }
        }
        boolean isStart = dc.read("$.paging.is_start");
        if (isStart){
            Integer totals = dc.read("$.paging.totals");
            for (int j = 1; j < totals; j++) {
                String nextUrl = String.format(Constants.USER_ANSWER_URL, userToken, j * 20);
                HttpRequestBase request = new HttpGet(nextUrl);
                request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
                zhiHuHttpClient.getAnswerPageThreadPool().execute(new PicAnswerTask(request, true, userToken));
            }
        }
    }

    private List<Answer> parseAnswers(DocumentContext dc){
        Integer answerCount = dc.read("$.data.length()");
        List<Answer> answerList = new ArrayList<>(answerCount);
        for(int i = 0; i < answerCount; i++){
            Answer answer = new Answer();
            String content = dc.read("$.data[" + i + "].content");
            Integer answerId = dc.read("$.data[" + i + "].id");
            Integer questionId = dc.read("$.data[" + i + "].question.id");
            String questionTitle = dc.read("$.data[" + i + "].question.title");
            answer.setContent(content);
            answer.setAnswerId(answerId);
            answer.setQuestionId(questionId);
            answer.setQuestionTitle(questionTitle);
            String url = "https://www.zhihu.com/question/%s/answer/%s";
            url = String.format(url, questionId, answerId);
            answer.setUrl(url);
            answerList.add(answer);
        }
        return answerList;
    }
}
