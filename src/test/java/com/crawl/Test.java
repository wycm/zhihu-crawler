package com.crawl;

import org.apache.http.client.methods.HttpGet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static int j = 0;
    public static synchronized void add(){
        j++;
    }
    public static void main(String[] args){
        String url = "http://www1.folha.uol.com.br/mundo/?cmpid=menutopo";
        String url1 = "http://pt.wikihow.com/P%C3%A1gina-principal";
        String url2 = "google.com.br";
        Pattern pattern = Pattern.compile("(.*?://)*((www[^\\.]*)*\\.)*(.*)\\.com.*");
        Matcher matcher = pattern.matcher(url2);
        if (matcher.find()){
            System.out.println(matcher.group(4));
        }
        System.out.println(new HttpGet("https://www.baidu.com/user").getURI());
    }
}
