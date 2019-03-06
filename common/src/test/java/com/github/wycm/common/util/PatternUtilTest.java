package com.github.wycm.common.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wycm on 2019-03-06.
 */
public class PatternUtilTest {

    @Test
    public void group() {
        String s = "https://www.zhihu.com/";
        String regex = "//.*?([^\\.]+)\\.(com|net|org|info|coop|int|co\\.uk|org\\.uk|ac\\.uk|uk|cn)";
        String result = PatternUtil.group(s, regex, 1);
        String result1 = PatternUtil.group("http://cn-proxy.com/", regex, 1);
        Assert.assertEquals(result, "zhihu");
        Assert.assertEquals(result1, "cn-proxy");
    }
}