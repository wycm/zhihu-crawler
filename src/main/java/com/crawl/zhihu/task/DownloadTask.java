package com.crawl.zhihu.task;

import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Proxy;
import com.crawl.zhihu.entity.Page;
import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 下载网页任务
 * 下载网页，并下载成功的Page放到解析线程池
 */
public class DownloadTask implements Runnable{
	private static Logger logger = SimpleLogger.getSimpleLogger(DownloadTask.class);
	private String url;
	private HttpRequestBase request;
	private boolean proxyFlag;//是否通过代理下载

	private static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
	public DownloadTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}
	public DownloadTask(HttpRequestBase request, boolean proxyFlag){
		this.request = request;
		this.proxyFlag = proxyFlag;
	}
	public void run(){
		try {
			Page page = null;
			if(url != null){
				if (proxyFlag){
					HttpGet request = new HttpGet(url);
					Proxy p = ProxyPool.queue.take();
					HttpHost proxy = new HttpHost(p.getIp(), p.getPort());
					request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
					page = zhiHuHttpClient.getWebPage(request);
				}else {
					page = zhiHuHttpClient.getWebPage(url);
				}
			}
			if(request != null){
				if (proxyFlag){
					Proxy p = ProxyPool.queue.take();
					HttpHost proxy = new HttpHost(p.getIp(), p.getPort());
					request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
					page = zhiHuHttpClient.getWebPage(request);
				}else {
					page = zhiHuHttpClient.getWebPage(request);
				}
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
					zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url, true));
				}
				if(request != null){
					zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(request, true));
				}
				return ;
			}
			logger.error(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
