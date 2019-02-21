package com.github.wycm.common;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by wycm on 2018/10/22.
 */
public interface SinglePool {
    String getCurrentThreadPoolName();

    ThreadPoolExecutor getCurrentThreadPool();
}
