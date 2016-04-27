package com.crawl.zhihu.crawlimganswer;

import com.crawl.util.MyLogger;
import com.crawl.zhihu.client.Href;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池执行的任务
 * 抓取网页
 * @author Administrator
 */
public class GetWebPageTask implements Runnable{
	private static Logger logger = MyLogger.getMyLogger(GetWebPageTask.class);
	private HttpGet getMethod = null;
	public static int gwpCount = 0;
	ImgAnswerStorage storage = null;
	ZhihuHttpClient zhClient = null;
	ThreadPoolExecutor gwpThreadPool = null;//获取网页线程池
	MyThreadPoolExecutor pwpThreadPool = null;//解析网页线程池
	public GetWebPageTask(ZhihuHttpClient zhClient, HttpGet httpGet){

	}
	public GetWebPageTask(ZhihuHttpClient zhClient, HttpGet getMethod, ImgAnswerStorage storage, ThreadPoolExecutor gwpThreadPool, MyThreadPoolExecutor pwpThreadPool){
		// TODO Auto-generated constructor stub
		this.zhClient = zhClient;
		this.getMethod = getMethod;
		this.storage = storage;
		this.gwpThreadPool = gwpThreadPool;
		this.pwpThreadPool = pwpThreadPool;
	}
	public void run()
	{
		CloseableHttpResponse response = null;
		CloseableHttpClient hc = zhClient.getHttpClient();
		try {
			// 执行getMethod
			response = hc.execute(getMethod,zhClient.getContext());
			int status = response.getStatusLine().getStatusCode();
			logger.info("executing request " + getMethod.getURI() + "   status:" + status);
			while(status == 429){
				//如果状态码为 429，则继续发起该请求
				Thread.sleep(200);
				response = hc.execute(getMethod,zhClient.getContext());
				status = response.getStatusLine().getStatusCode();
				logger.info("executing request " + getMethod.getURI() + "   status:" + status);
				if(status != 429){
					break;
				}
			}if(status == HttpStatus.SC_OK){
				gwpCount++;
				String s = IOUtils.toString(response.getEntity().getContent());
				Href h = new Href(s,getMethod.getURI().toString(), Jsoup.parse(s).title());
				storage.push(h);//入队
				pwpThreadPool.execute(new ParseImgAnswerTask(zhClient,this.storage,gwpThreadPool,pwpThreadPool));
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("ClientProtocolException",e);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("ConnectException",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("IOException",e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("InterruptedException",e);
		} catch (NullPointerException e){
			e.printStackTrace();
			logger.error("NullPointerException",e);
		}finally {
			// 释放连接
			if(response.getEntity() != null){
				try {
					getMethod.releaseConnection();;
					response.getEntity().getContent().close();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("UnsupportedOperationException",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("IOException",e);
				}
			}
			if (response.getStatusLine ().getStatusCode () != 200) {
				getMethod.abort();
			}
		}

	}

}
