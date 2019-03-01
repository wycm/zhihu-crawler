package com.github.wycm.zhihu.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class AsyncHttpClientUtilTest {

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put(":scheme", "https");
        headers.put(":method", "GET");
    }
}
