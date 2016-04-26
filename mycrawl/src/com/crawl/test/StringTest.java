package com.crawl.test;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class StringTest {
    public static void main(String args []){
        String query = "acbac";
        String text = "acaccbabb";
        System.out.println("公共长度为:" + deal(query,text));
    }
    public static int deal(String query,String text){
        int max = 0;
        a:
        for(int i = text.length();i >= 0;i--){
            for(int j = 0;j < text.length();j++){
                String s = null;
                if(i + j <= text.length()){
                    s= text.substring(j,i + j);
                    if(query.contains(s)){
                        System.out.println(s);
                        return s.length();
                    }
                }
            }
        }
        return max;
    }
}
