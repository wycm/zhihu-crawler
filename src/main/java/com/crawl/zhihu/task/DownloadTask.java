package com.crawl.zhihu.task;

import com.crawl.zhihu.entity.Page;
import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

/**
 * 下载网页任务
 * 下载网页，并下载成功的Page放到解析线程池
 */
public class DownloadTask implements Runnable{
	private static Logger logger = SimpleLogger.getSimpleLogger(DownloadTask.class);
	private String url;
	private HttpRequestBase request;
	private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
	public DownloadTask(String url){
		this.url = url;
	}
	public DownloadTask(HttpRequestBase request){
		this.request = request;
	}
	public void run(){
		try {
			Page page = null;
			if(url != null){
				page = zhiHuHttpClient.getWebPage(url);
			}
			if(request != null){
				page = zhiHuHttpClient.getWebPage(request);
			}
			int status = page.getStatusCode();
			logger.info(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
			if(status == HttpStatus.SC_OK){
				zhiHuHttpClient.getParseThreadExecutor().execute(new ParseTask(page));
				return;
			}
			else if(status == 502 || status == 504 || status == 500 || status == 429){
				/**
				 * 将请求继续加入线程池
                 */
				Thread.sleep(100);
				if(url != null){
					zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url));
				}
				if(request != null){
					zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(request));
				}
				return ;
			}
			logger.error(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
		} catch (InterruptedException e) {
			logger.error("InterruptedException",e);
		}
	}
}
