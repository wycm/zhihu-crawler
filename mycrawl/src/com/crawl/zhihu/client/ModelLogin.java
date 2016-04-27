package com.crawl.zhihu.client;

import com.crawl.util.MyLogger;
import com.crawl.util.chcUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/4/8 0008.
 * 模拟登录知乎
 */
public class ModelLogin {
    private static Logger logger = MyLogger.getMyLogger(ModelLogin.class);
    /**
     * 肉眼识别验证码
     * @param httpClient
     * @param url 验证码地址
     * @return
     */
    public String yzm(CloseableHttpClient httpClient, String url){
        chcUtils.downloadFile(httpClient, url, "d:/test/", "1.gif",true);
        Scanner sc = new Scanner(System.in);
        String yzm = sc.nextLine();
        return yzm;
    }
    /**
     * 模拟登录知乎
     * 登录成功后将cookie序列化到本地
     * @throws Exception
     */
    public void login(CloseableHttpClient httpClient, HttpClientContext context){
        String yzm = null;
        String loginState = null;
        HttpGet getRequest = new HttpGet("http://www.zhihu.com/#signin");
        chcUtils.getWebPage(httpClient,context, getRequest, "utf-8", true);
        HttpPost request = new HttpPost("http://www.zhihu.com/login/email");
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        yzm = yzm(httpClient,"http://www.zhihu.com/captcha.gif");//肉眼识别验证码
        formParams.add(new BasicNameValuePair("captcha", yzm));
        formParams.add(new BasicNameValuePair("_xsrf", ""));
        formParams.add(new BasicNameValuePair("email", "1057160387@qq.com"));
        formParams.add(new BasicNameValuePair("password", "wangyang110."));
        formParams.add(new BasicNameValuePair("remember_me", "true"));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        loginState = chcUtils.getWebPage(httpClient,context, request, "utf-8", true);//登录
        JSONObject jo = new JSONObject(loginState);
        if(jo.get("r").toString().equals("0")){
            System.out.println("登录成功");
            getRequest = new HttpGet("https://www.zhihu.com");
            chcUtils.getWebPage(httpClient,context ,getRequest, "utf-8", true);//直接访问首页
            chcUtils.serializeObject(context.getCookieStore(),"resources/zhihucookies");//序列化知乎cookies，下次登录直接通过该cookies登录
        }else{
            System.out.println("登录失败");
        }
    }
    public static void main(String args []){
        ModelLogin ml = new ModelLogin();
        HttpClientContext context = chcUtils.getMyHttpClientContext();
        CloseableHttpClient httpClient = chcUtils.getMyHttpClient();
//        ml.login(httpClient,context);
        context.setCookieStore((CookieStore) chcUtils.antiSerializeMyHttpClient("resources/zhihucookies"));
        HttpGet getRequest = new HttpGet("https://www.zhihu.com");
        chcUtils.getWebPage(httpClient,context,getRequest,"utf-8",true);
    }
}
