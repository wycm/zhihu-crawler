package com.crawl.zhihu.client;

import com.crawl.util.MyLogger;
import com.crawl.util.HttpClientUtil;
import org.apache.http.NameValuePair;
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
 * 模拟登录知乎
 */
public class ModelLogin {
    private static Logger logger = MyLogger.getMyLogger(ModelLogin.class);
    /**
     *
     * @param httpClient Http客户端
     * @param context Http上下文
     * @return
     */
    public boolean login(CloseableHttpClient httpClient, HttpClientContext context){
        String yzm = null;
        String loginState = null;
        HttpGet getRequest = new HttpGet("https://www.zhihu.com/#signin");
        HttpClientUtil.getWebPage(httpClient,context, getRequest, "utf-8", false);
        HttpPost request = new HttpPost("https://www.zhihu.com/login/email");
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        yzm = yzm(httpClient, context,"https://www.zhihu.com/captcha.gif?type=login");//肉眼识别验证码
        formParams.add(new BasicNameValuePair("captcha", yzm));
        formParams.add(new BasicNameValuePair("_xsrf", ""));//这个参数可以不用
        formParams.add(new BasicNameValuePair("email", "邮箱"));
        formParams.add(new BasicNameValuePair("password", "密码"));
        formParams.add(new BasicNameValuePair("remember_me", "true"));
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        loginState = HttpClientUtil.getWebPage(httpClient,context, request, "utf-8", false);//登录
        JSONObject jo = new JSONObject(loginState);
        if(jo.get("r").toString().equals("0")){
            System.out.println("登录成功");
            getRequest = new HttpGet("https://www.zhihu.com");
            HttpClientUtil.getWebPage(httpClient,context ,getRequest, "utf-8", false);//访问首页
            HttpClientUtil.serializeObject(context.getCookieStore(),"resources/zhihucookies");//序列化知乎Cookies，下次登录直接通过该cookies登录
            return true;
        }else{
            System.out.println("登录失败" + loginState);
            return false;
        }
    }
    /**
     * 肉眼识别验证码
     * @param httpClient Http客户端
     * @param context Http上下文
     * @param url 验证码地址
     * @return
     */
    public String yzm(CloseableHttpClient httpClient,HttpClientContext context, String url){
        HttpClientUtil.downloadFile(httpClient, context, url, "d:/test/", "1.gif",true);
        Scanner sc = new Scanner(System.in);
        String yzm = sc.nextLine();
        return yzm;
    }
    public static void main(String args []){
        ModelLogin ml = new ModelLogin();
        HttpClientContext context = HttpClientUtil.getMyHttpClientContext();
        CloseableHttpClient httpClient = HttpClientUtil.getMyHttpClient();
        ml.login(httpClient,context);
//        context.setCookieStore((CookieStore) chcUtils.antiSerializeMyHttpClient("resources/zhihucookies"));
        HttpGet getRequest = new HttpGet("https://www.zhihu.com");
        HttpClientUtil.getWebPage(httpClient,context,getRequest,"utf-8",false);
    }
}
