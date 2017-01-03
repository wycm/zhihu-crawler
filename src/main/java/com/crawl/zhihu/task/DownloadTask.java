package com.crawl.zhihu.task;

import com.crawl.core.util.Config;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Direct;
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
 * 下载网页任务， 并下载成功的Page放到解析线程池
 * 若使用代理，从ProxyPool中取
 * @see ProxyPool
 */
public class DownloadTask implements Runnable{
	private static Logger logger = SimpleLogger.getSimpleLogger(DownloadTask.class);
	private String url;
	private HttpRequestBase request;
	private boolean proxyFlag;//是否通过代理下载
	private Proxy currentProxy;//当前线程使用的代理

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
					HttpGet tempReqeust = new HttpGet(url);
					currentProxy = ProxyPool.proxyQueue.take();
					if(!(currentProxy instanceof Direct)){
						HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
						tempReqeust.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
					}
					page = zhiHuHttpClient.getWebPage(tempReqeust);
				}else {
					page = zhiHuHttpClient.getWebPage(url);
				}
			}
			if(request != null){
				if (proxyFlag){
					currentProxy = ProxyPool.proxyQueue.take();
					if(!(currentProxy instanceof Direct)) {
						HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
						request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
					}
					page = zhiHuHttpClient.getWebPage(request);
				}else {
					page = zhiHuHttpClient.getWebPage(request);
				}
			}
			int status = page.getStatusCode();
			logger.info(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
			if(status == HttpStatus.SC_OK){
				zhiHuHttpClient.getParseThreadExecutor().execute(new ParseTask(page));
				if(currentProxy != null){
					/**
					 * 该代理可用，将该代理继续添加到proxyQueue
					 */
					currentProxy.setDelayTime(5000l);
					ProxyPool.proxyQueue.add(currentProxy);//将当前代理放入代理池中
				}
				return;
			}
			else if(status > 404){
				Thread.sleep(100);
				retry();
				return ;
			}
			logger.error(Thread.currentThread().getName() + " executing request " + page.getUrl() + "   status:" + status);
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (IOException e) {
			retry();
		} finally {
			if (request != null){
				request.releaseConnection();
			}
		}
	}

	/**
	 * retry
	 */
	private void retry(){
		if(url != null){
			zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(url, Config.isProxy));
		}
		if (request != null){
			zhiHuHttpClient.getDownloadThreadExecutor().execute(new DownloadTask(request, Config.isProxy));
		}
	}
}
