package com.github.wycm.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wycm on 2018/10/24.
 */
public class PatternUtil {
    private static Map<String, Pattern> map = new ConcurrentHashMap<>();
    public static String group(String content, String patternStr, int group){
        Pattern pattern = null;
        if (!map.containsKey(patternStr)){
            pattern = Pattern.compile(patternStr);
        } else {
            pattern = map.get(patternStr);
        }
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()){
            return matcher.group(group);
        }
        return null;
    }
}
