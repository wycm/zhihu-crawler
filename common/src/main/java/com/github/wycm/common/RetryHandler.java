package com.github.wycm.common;

/**
 * Created by wycm on 2018/10/16.
 */
public interface RetryHandler {


    /**
     *
     */
    void retry() throws InterruptedException;

    /**
     * 最大重试次数
     * @return
     */
    int getMaxRetryTimes();

    /**
     * 当前重试次数
     * @return
     */
    int getCurrentRetryTimes();
}
