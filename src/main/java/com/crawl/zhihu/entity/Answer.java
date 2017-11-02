package com.crawl.zhihu.entity;


public class Answer {
    /**
     * answer id
     */
    private Integer answerId;
    /**
     * question id
     */
    private Integer questionId;
    /**
     * 答案
     */
    private String content;

    /**
     * 答案url
     */
    private String url;

    private String questionTitle;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                '}';
    }
}
