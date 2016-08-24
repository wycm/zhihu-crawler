package com.crawl.zhihu.task;

import com.crawl.entity.Page;
import com.crawl.util.MyLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.ZhiHuTaskManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;

/**
 * 下载网页任务，负责执行request，将response的网页内容加入Storage
 */
public class DownloadTask implements Runnable{
	private static Logger logger = MyLogger.getMyLogger(DownloadTask.class);
	private ZhiHuTaskManager zhiHuTaskManager;
	private String url;
	private ZhiHuHttpClient zhiHuHttpClient;

	public DownloadTask(ZhiHuTaskManager zhiHuTaskManager,
						ZhiHuHttpClient zhiHuHttpClient,
						String url){
		this.zhiHuTaskManager = zhiHuTaskManager;
		this.url = url;
	}
	public void run(){
		CloseableHttpResponse response = null;
		CloseableHttpClient hc = zhiHuHttpClient.getCloseableHttpClient();
		try {
			Page page = zhiHuHttpClient.getWebPage(url);
			int status = page.getStatusCode();
			logger.info("executing request " + page.getUrl() + "   status:" + status);
			if(status == HttpStatus.SC_OK){
				String content = IOUtils.toString(response.getEntity().getContent());
//				zhiHuTaskManager.execute(new ParseTask(zhiHuTaskManager,
//						zhiHuHttpClient,
//						content));
			}
			else if(status == 502 || status == 504 || status == 500 || status == 429){
				//如果状态码50X，将该请求继续加入线程池
				Thread.sleep(100);
//				gwpThreadPool.execute(new DownloadTask(zhClient,getMethod,storage,gwpThreadPool,pwpThreadPool));
				return ;
			}
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException",e);
		} catch (ConnectException e) {
			logger.error("ConnectException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException",e);
		} catch (NullPointerException e){
			logger.error("NullPointerException",e);
		}finally {

		}

	}

}
