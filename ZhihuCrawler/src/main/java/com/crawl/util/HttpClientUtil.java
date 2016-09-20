package com.crawl.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 */
public class HttpClientUtil {
	private static Logger logger = MyLogger.getMyLogger(HttpClientUtil.class);
	/**
	 *
	 * @param httpClient HttpClient客户端
	 * @param context 上下文
	 * @param request 请求
	 * @param encoding 字符编码
	 * @param isPrintConsole 是否打印到控制台
     * @return 网页内容
     */
	public static String getWebPage(CloseableHttpClient httpClient
			, HttpClientContext context
			, HttpRequestBase request
			, String encoding
			, boolean isPrintConsole){
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request,context);
		} catch (HttpHostConnectException e){
			e.printStackTrace();
			logger.error("HttpHostConnectException",e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IOException",e);
		}
		logger.info("status---" + response.getStatusLine().getStatusCode());
		BufferedReader rd = null;
		StringBuilder webPage = null;
		try {
			rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(),encoding));
			String line = "";
			webPage = new StringBuilder();
			while((line = rd.readLine()) != null) {
				webPage.append(line);
				if(isPrintConsole){
					logger.info(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.releaseConnection();
		return webPage.toString();
	}
	/**
	 * 序列化对象
	 * @param object
	 * @throws Exception
	 */
	public static void serializeObject(Object object,String filePath){
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			logger.info("序列化成功");
			oos.flush();
			fos.close();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 反序列化对象
	 * @param path
	 * @throws Exception
	 */
	public static Object deserializeMyHttpClient(String path) throws NullPointerException, FileNotFoundException {
//		InputStream fis = HttpClientUtil.class.getResourceAsStream(name);
        File file = new File(path);
		InputStream fis = new FileInputStream(file);
		ObjectInputStream ois = null;
		Object object = null;
		try {
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			fis.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
	/**
	 * 设置Cookies策略
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient getMyHttpClient(){
		CloseableHttpClient httpClient = null;
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
				.build();
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig)
				.build();
		return httpClient;
	}
	/**
	 * 设置上下文
	 * @return HttpClientContext
	 */
	public static HttpClientContext getMyHttpClientContext(){
		HttpClientContext context = null;
		context = HttpClientContext.create();
		Registry<CookieSpecProvider> registry = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory()).build();
		context.setCookieSpecRegistry(registry);
		return context;
	}
	/**
	 * 下载图片
	 * @param fileURL 文件地址
	 * @param path 保存路径
	 * @param saveFileName 文件名，包括后缀名
	 * @param isReplaceFile 若存在文件时，是否还需要下载文件
	 */
	public static void downloadFile(CloseableHttpClient httpClient
			, HttpClientContext context
			, String fileURL
			, String path
			, String saveFileName
			, Boolean isReplaceFile){
		try{
			HttpGet request = new HttpGet(fileURL);
			CloseableHttpResponse response = httpClient.execute(request,context);
			logger.info("status:" + response.getStatusLine().getStatusCode());
			File file =new File(path);
			//如果文件夹不存在则创建
			if  (!file .exists()  && !file .isDirectory()){
				//logger.info("//不存在");
				file.mkdirs();
			} else{
				logger.info("//目录存在");
			}
			file = new File(path + saveFileName);
			if(!file.exists() || isReplaceFile){
				//如果文件不存在，则下载
				try {
					OutputStream os = new FileOutputStream(file);
					InputStream is = response.getEntity().getContent();
					byte[] buff = new byte[(int) response.getEntity().getContentLength()];
					while(true) {
						int readed = is.read(buff);
						if(readed == -1) {
							break;
						}
						byte[] temp = new byte[readed];
						System.arraycopy(buff, 0, temp, 0, readed);
						os.write(temp);
						logger.info("文件下载中....");
					}
					is.close();
					os.close();
					logger.info(fileURL + "--文件成功下载至" + path + saveFileName);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				logger.info(path);
				logger.info("该文件存在！");
			}
			request.releaseConnection();
		} catch(IllegalArgumentException e){
			logger.info("连接超时...");

		} catch(Exception e1){
			e1.printStackTrace();
		}
	}
	/**
	 * 输出Cookies
	 * @param cs
	 */
	public static void getCookies(CookieStore cs){
		List<Cookie> cookies = cs.getCookies();
		if(cookies == null){
			logger.info("该CookiesStore无Cookie");
		}else{
			for(int i = 0;i < cookies.size();i++){
				logger.info("cookie：" + cookies.get(i).getName() + ":"+ cookies.get(i).getValue()
						+ "----过期时间"+ cookies.get(i).getExpiryDate()
						+ "----Comment"+ cookies.get(i).getComment()
						+ "----CommentURL"+ cookies.get(i).getCommentURL()
						+ "----domain"+ cookies.get(i).getDomain()
						+ "----ports"+ cookies.get(i).getPorts()
				);
			}
		}
	}
	public static void getAllHeaders(Header [] headers){
		logger.info("------标头开始------");
		for(int i = 0;i < headers.length;i++){
			logger.info(headers[i]);
		}
		logger.info("------标头结束------");
	}
	/**
	 * InputStream转换为String
	 * @param is
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String isToString(InputStream is,String encoding) throws Exception{
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, encoding);
		return writer.toString();
	}
	/**
	 * 有bug 慎用
	 * unicode转化String
	 * @return
     */
	public static String decodeUnicode(String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			start = dataStr.indexOf("\\u", start - (6 - 1));
			if (start == -1){
				break;
			}
			start = start + 2;
			end = start + 4;
			String tempStr = dataStr.substring(start, end);
			String charStr = "";
			charStr = dataStr.substring(start, end);
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			dataStr = dataStr.replace("\\u" + tempStr, letter + "");
			start = end;
		}
		logger.debug(dataStr);
		return dataStr;
	}
	/**
	 * 设置request请求参数
	 * @param request
	 * @param params
     */
	public static void setHttpPostParams(HttpPost request,Map<String,String> params) throws UnsupportedEncodingException {
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			formParams.add(new BasicNameValuePair(key,params.get(key)));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "utf-8");
		request.setEntity(entity);
	}
	public static void main(String args []){
		String s = "{    \"r\": 1,    \"errcode\": 100000,        \"data\": {\"account\":\"\\u5e10\\u53f7\\u6216\\u5bc6\\u7801\\u9519\\u8bef\"},            \"msg\": \"\\u8be5\\u624b\\u673a\\u53f7\\u5c1a\\u672a\\u6ce8\\u518c\\u77e5\\u4e4e";
		logger.info(decodeUnicode(s));
	}
}
