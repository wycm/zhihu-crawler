package com.crawl.util;

import com.crawl.httpclient.HttpClient;
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
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * HttpClient工具类
 */
public class HttpClientUtil {
	private static Logger logger = SimpleLogger.getSimpleLogger(HttpClientUtil.class);
    private static CookieStore cookieStore = new BasicCookieStore();
	private static CloseableHttpClient httpClient;
	private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
	private static HttpHost proxy;
	private static RequestConfig requestConfig;

	static{
		init();
	}
	private static void init() {
        try {
            SSLContext sslContext =
                    SSLContexts.custom()
                            .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                                @Override
                                public boolean isTrusted(X509Certificate[] chain, String authType)
                                        throws CertificateException {
                                    return true;
                                }
                            }).build();
            SSLConnectionSocketFactory sslSFactory =
                    new SSLConnectionSocketFactory(sslContext);
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslSFactory)
                            .build();

            PoolingHttpClientConnectionManager connManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            ConnectionConfig connectionConfig =
                    ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                            .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(300);
            connManager.setDefaultMaxPerRoute(100);
            HttpClientBuilder httpClientBuilder =
                    HttpClients.custom().setConnectionManager(connManager)
                            .setDefaultCookieStore(new BasicCookieStore()).setUserAgent(userAgent);
            if (proxy != null) {
                httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
            }
            httpClient = httpClientBuilder.build();

            requestConfig = RequestConfig.custom().setSocketTimeout(10000).
					setConnectTimeout(10000).
					setConnectionRequestTimeout(10000).
					setCookieSpec(CookieSpecs.STANDARD).
					build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

	public static String getWebPage(String url){
		HttpGet request = new HttpGet(url);
		return getWebPage(request, "utf-8", false);
	}
	public static String postRequest(String postUrl, Map<String, String> params){
		HttpPost post = new HttpPost(postUrl);
		setHttpPostParams(post, params);
		return getWebPage(post, "utf-8", false);
	}
	/**
	 * @param encoding 字符编码
	 * @param isPrintConsole 是否打印到控制台
     * @return 网页内容
     */
	public static String getWebPage(HttpRequestBase request
			, String encoding
			, boolean isPrintConsole){
		CloseableHttpResponse response = null;
		try {
			response = getResponse(request);
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
	private static CloseableHttpResponse getResponse(HttpRequestBase request) throws IOException {
		request.setConfig(requestConfig);
		HttpClientContext httpClientContext = HttpClientContext.create();
		httpClientContext.setCookieStore(cookieStore);
		CloseableHttpResponse response = httpClient.execute(request, httpClientContext);
		return response;
	}
	public static CloseableHttpResponse getResponse(String url) throws IOException {
		HttpGet request = new HttpGet(url);
		return getResponse(request);
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
	 * 下载图片
	 * @param fileURL 文件地址
	 * @param path 保存路径
	 * @param saveFileName 文件名，包括后缀名
	 * @param isReplaceFile 若存在文件时，是否还需要下载文件
	 */
	public static void downloadFile(String fileURL
			, String path
			, String saveFileName
			, Boolean isReplaceFile){
		try{
			CloseableHttpResponse response = getResponse(fileURL);
			logger.info("status:" + response.getStatusLine().getStatusCode());
			File file =new File(path);
			//如果文件夹不存在则创建
			if  (!file .exists()  && !file .isDirectory()){
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
			response.close();
		} catch(IllegalArgumentException e){
			logger.info("连接超时...");
		} catch(Exception e1){
			e1.printStackTrace();
		}
	}
	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(CookieStore cookieStore) {
		HttpClientUtil.cookieStore = cookieStore;
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
	public static void setHttpPostParams(HttpPost request,Map<String,String> params){
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			formParams.add(new BasicNameValuePair(key,params.get(key)));
		}
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formParams, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
	}
	public static void main(String args []){
		String s = "{    \"r\": 1,    \"errcode\": 100000,        \"data\": {\"account\":\"\\u5e10\\u53f7\\u6216\\u5bc6\\u7801\\u9519\\u8bef\"},            \"msg\": \"\\u8be5\\u624b\\u673a\\u53f7\\u5c1a\\u672a\\u6ce8\\u518c\\u77e5\\u4e4e";
		logger.info(decodeUnicode(s));
	}
}
