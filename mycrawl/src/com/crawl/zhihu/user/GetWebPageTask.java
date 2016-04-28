package com.crawl.zhihu.user;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ThreadPoolExecutor;
import com.crawl.util.MyLogger;
import com.crawl.zhihu.client.ZhihuHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

/**
 * 获取网页线程，负责执行request，将response的网页内容加入Storage
 */
public class GetWebPageTask implements Runnable{
	private static Logger logger = MyLogger.getMyLogger(GetWebPageTask.class);
	private HttpGet getMethod = null;
	public static int gwpCount = 0;
	Storage storage = null;
	ZhihuHttpClient zhClient = null;
	ThreadPoolExecutor gwpThreadPool = null;//获取网页线程池
	MyThreadPoolExecutor pwpThreadPool = null;//解析网页线程池
	public GetWebPageTask(){

	}
	public GetWebPageTask(ZhihuHttpClient zhClient, HttpGet getMethod, Storage storage,ThreadPoolExecutor gwpThreadPool,MyThreadPoolExecutor pwpThreadPool){
		// TODO Auto-generated constructor stub
		this.zhClient = zhClient;
		this.getMethod = getMethod;
		this.storage = storage;
		this.gwpThreadPool = gwpThreadPool;
		this.pwpThreadPool = pwpThreadPool;
	}
	public void run(){
		CloseableHttpResponse response = null;
		CloseableHttpClient hc = zhClient.getHttpClient();
		try {
			// 执行getMethod
			response = hc.execute(getMethod,zhClient.getContext());
			int status = response.getStatusLine().getStatusCode();
			logger.error("executing request " + getMethod.getURI() + "   status:" + status);
			while(status == 429){
				//如果状态码为 429，则继续发起该请求
				Thread.sleep(100);
				response = hc.execute(getMethod,zhClient.getContext());
				status = response.getStatusLine().getStatusCode();
				if(status != 429){
					break;
				}
			}
			if(status == HttpStatus.SC_OK){
				gwpCount++;
				String s = IOUtils.toString(response.getEntity().getContent());
				storage.push(s);//入队
				pwpThreadPool.execute(new ParseWebPageTask(zhClient,this.storage,gwpThreadPool,pwpThreadPool));
			} else if(status == 502 || status == 504){
				return ;
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
					getMethod.releaseConnection();
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
			if (response.getStatusLine().getStatusCode() != 200) {
				getMethod.abort();
			}
		}

	}

}
