package com.crawl;


import com.crawl.zhihu.task.AbstractPageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExceptionTest {
    private static Logger logger = LoggerFactory.getLogger(AbstractPageTask.class);
    public static void main(String[] args){
        try {
            throw new IOException();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
