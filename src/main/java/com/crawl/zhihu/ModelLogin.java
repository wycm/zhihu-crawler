package com.crawl.zhihu;

import com.crawl.config.Config;
import com.crawl.util.HttpClientUtil;
import com.crawl.util.SimpleLogger;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 模拟登录知乎
 */
public class ModelLogin {
    private static Logger logger = SimpleLogger.getSimpleLogger(ModelLogin.class);
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
        String yzm = null;
        String loginState = null;
        HttpGet getRequest = new HttpGet(INDEX_URL);
        HttpClientUtil.getWebPage(getRequest, "utf-8", false);
        Map<String, String> postParams = new HashMap<>();
        yzm = yzm(YZM_URL);//肉眼识别验证码
        postParams.put("captcha", yzm);
        postParams.put("_xsrf", "");//这个参数可以不用
        postParams.put("password", pwd);
        postParams.put("remember_me", "true");
        if(emailOrPhoneNum.contains("@")){
            //通过邮箱登录
            postParams.put("email", emailOrPhoneNum);
            loginState = HttpClientUtil.postRequest(EMAIL_LOGIN_URL, postParams);//登录
        }
        else {
            //通过手机号码登录
            postParams.put("phone_num", emailOrPhoneNum);
            loginState = HttpClientUtil.postRequest(PHONENUM_LOGIN_URL, postParams);//登录
        }

        JSONObject jo = new JSONObject(loginState);
        if(jo.get("r").toString().equals("0")){
            logger.info("登录知乎成功");
            /**
             * 序列化Cookies
             */
            HttpClientUtil.serializeObject(HttpClientUtil.getCookieStore(), Config.cookiePath);
            return true;
        }else{
            logger.info("登录知乎失败");
            throw new RuntimeException(HttpClientUtil.decodeUnicode(loginState));
        }
    }
    /**
     * 肉眼识别验证码
     * @param url 验证码地址
     * @return
     */
    public String yzm(String url){
        String verificationCodePath = Config.verificationCodePath;
        String path = verificationCodePath.substring(0, verificationCodePath.lastIndexOf("/") + 1);
        String fileName = verificationCodePath.substring(verificationCodePath.lastIndexOf("/") + 1);
        HttpClientUtil.downloadFile(url, path, fileName,true);
        logger.info("请输入 " + verificationCodePath + " 下的验证码：");
        Scanner sc = new Scanner(System.in);
        String yzm = sc.nextLine();
        return yzm;
    }
}
