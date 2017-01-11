package com.crawl;


import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.task.PageTask;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExceptionTest {
    private static Logger logger = SimpleLogger.getSimpleLogger(PageTask.class);
    public static void main(String[] args){
        try {
            throw new IOException();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
