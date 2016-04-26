package com.crawl.test;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;

public class LogTest {
    private static Logger logger = MyLogger.getMyLogger(LogTest.class);
    private int i = 0;
    public static void main(String args []){
        LogTest d  = null;
        try{
            logger.debug("test");
            System.out.println(d.i);
        }catch (NullPointerException e){
            logger.error("exception ",e);
        }
    }
}
