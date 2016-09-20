package com.crawl.zhihu;

import com.crawl.config.Config;
import com.crawl.util.HttpClientUtil;
import com.crawl.util.MyLogger;
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
    //知乎首页
    final private static String INDEX_URL = "https://www.zhihu.com";
    //邮箱登录地址
    final private static String EMAIL_LOGIN_URL = "https://www.zhihu.com/login/email";
    //手机号码登录地址
    final private static String PHONENUM_LOGIN_URL = "https://www.zhihu.com/login/phone_num";
    //登录验证码地址
    final private static String YZM_URL = "https://www.zhihu.com/captcha.gif?type=login";
    /**
     *
     * @param emailOrPhoneNum 邮箱或手机号码
     * @param pwd 密码
     * @return
     */
    public boolean login(ZhiHuHttpClient zhiHuHttpClient,
                         String emailOrPhoneNum,
                         String pwd){
        CloseableHttpClient httpClient = zhiHuHttpClient.getCloseableHttpClient();
        HttpClientContext context = zhiHuHttpClient.getHttpClientContext();
        String yzm = null;
        String loginState = null;
        HttpGet getRequest = new HttpGet(INDEX_URL);
        HttpClientUtil.getWebPage(httpClient,context, getRequest, "utf-8", false);
        HttpPost request = null;
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        if(emailOrPhoneNum.contains("@")){
            //通过邮箱登录
            request = new HttpPost(EMAIL_LOGIN_URL);
            formParams.add(new BasicNameValuePair("email", emailOrPhoneNum));
        }
        else {
            //通过手机号码登录
            request = new HttpPost(PHONENUM_LOGIN_URL);
            formParams.add(new BasicNameValuePair("phone_num", emailOrPhoneNum));
        }
        yzm = yzm(httpClient, context,YZM_URL);//肉眼识别验证码
        formParams.add(new BasicNameValuePair("captcha", yzm));
        formParams.add(new BasicNameValuePair("_xsrf", ""));//这个参数可以不用
        formParams.add(new BasicNameValuePair("password", pwd));
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
            logger.info("登录知乎成功");
            getRequest = new HttpGet("https://www.zhihu.com");
            /**
             * 访问首页
             */
            HttpClientUtil.getWebPage(httpClient,context ,getRequest, "utf-8", false);
            /**
             * 序列化Cookies
             */
            HttpClientUtil.serializeObject(context.getCookieStore(), Config.cookiePath);
            return true;
        }else{
            logger.info("登录知乎失败");
            throw new RuntimeException(HttpClientUtil.decodeUnicode(loginState));
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
        String verificationCodePath = Config.verificationCodePath;
        String path = verificationCodePath.substring(0, verificationCodePath.lastIndexOf("/") + 1);
        String fileName = verificationCodePath.substring(verificationCodePath.lastIndexOf("/") + 1);
        HttpClientUtil.downloadFile(httpClient, context, url, path, fileName,true);
        logger.info("请输入 " + verificationCodePath + " 下的验证码：");
        Scanner sc = new Scanner(System.in);
        String yzm = sc.nextLine();
        return yzm;
    }
}
