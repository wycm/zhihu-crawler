package com.github.wycm.proxy;

import com.github.wycm.common.*;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.ThreadPoolUtil;
import com.github.wycm.common.util.CrawlerUtils;
import com.github.wycm.proxy.util.ProxyUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Request;
import org.asynchttpclient.proxy.ProxyServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;


/**
 * page task
 * 下载网页并解析，具体解析由子类实现
 * 若使用代理，从ProxyPool中取
 */
@Slf4j
public abstract class AbstractPageTask implements Runnable, RetryHandler, SinglePool {
	@Getter@Setter
	protected String url;

	protected Request asyncRequest;

	@Getter
	protected final static Function<String, Boolean> jsonPageBannedFunction = (String s) -> !s.contains("login_status");

	@Getter
	protected final static Function<String, Boolean> htmlPageBannedFunction = (String s) -> s.equals("<html><header></header><body></body></html>");

	@Getter@Setter
	protected boolean proxyFlag;//是否通过代理下载

	protected Proxy currentProxy;//当前线程使用的代理

	@Getter@Setter
	protected int currentRetryTimes = 0;

	private String currentPoolName = null;


	//当前任务是否需要重试
	@Getter@Setter
	boolean isRetry = false;

	@Getter@Setter
	protected CrawlerMessage crawlerMessage;


	public AbstractPageTask(){

	}
	public AbstractPageTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}

	public AbstractPageTask(String url, boolean proxyFlag, int currentRetryTimes){
		this.url = url;
		this.proxyFlag = proxyFlag;
		this.currentRetryTimes = currentRetryTimes;
	}

	public AbstractPageTask(CrawlerMessage message){
		this.crawlerMessage = message;
	}
	public void run(){
		long requestStartTime = 0l;
		try {
			Page page = null;
			if(url != null){
				if (proxyFlag){
					currentProxy = getProxyQueue().takeProxy(getProxyQueueName());
					requestStartTime = System.currentTimeMillis();

					if(!(currentProxy.getIp().equals(getLocalIPService().getLocalIp()))){
						//代理
						page = getHttpClient().asyncGet(url, new ProxyServer.Builder(currentProxy.getIp(), currentProxy.getPort()).build(), crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
					} else {
						page = getHttpClient().asyncGet(url, crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
					}
				}else {
					requestStartTime = System.currentTimeMillis();
					page = getHttpClient().asyncGet(url, crawlerMessage.getUserAgent(), crawlerMessage.getHeaders());
				}
			}
			long requestEndTime = System.currentTimeMillis();
			page.setProxy(currentProxy);
			int status = page.getStatusCode();
			String logStr = Thread.currentThread().getName() + " " + currentProxy +
					"  executing request " + page.getUrl()  + " response statusCode:" + status +
					"  request cost time:" + (requestEndTime - requestStartTime) + "ms";
			if(status == 200 && !responseError(page)){
				ProxyUtil.handleResponseSuccProxy(currentProxy);
				handle(page);
			} else {
				log.error(logStr);
				ProxyUtil.handleResponseFailedProxy(currentProxy);
				retry();
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			log.debug(e.getMessage(), e);
			ProxyUtil.handleResponseFailedProxy(currentProxy);
			try {
				retry();
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
			}
		} catch (Exception e){
			log.error(e.getMessage(), e);
		} finally {
			if (currentProxy != null && !ProxyUtil.isDiscardProxy(currentProxy)){
				getProxyQueue().addProxy(getProxyQueueName(), currentProxy);
			}
			try {
			    receiveNewTask();
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}




	/**
	 * 子类实现page的处理
	 * @param page
	 */
	protected abstract void handle(Page page);


	protected void receiveNewTask() throws InterruptedException {
		if (crawlerMessage != null && !Constants.stopService && !isRetry){
			CrawlerMessage message = getTaskQueueService().receiveTask(CrawlerUtils.getTaskQueueName(this.getClass()));
			createNewTask(message);
		}
	}

	protected abstract void createNewTask(CrawlerMessage crawlerMessage);

	@Override
	public String getCurrentThreadPoolName() {
		if (this.currentPoolName == null){
			this.currentPoolName = ThreadPoolUtil.getThreadPoolName(this.getClass());
		}
		return this.currentPoolName;
	}

	@Override
	public ThreadPoolExecutor getCurrentThreadPool(){
		return ThreadPoolUtil.getThreadPoolExecutorMap().get(getCurrentThreadPoolName());
	}

	private String getProxyStr(Proxy proxy){
		if (proxy == null){
			return "";
		}
		return proxy.getIp() + ":" + proxy.getPort();
	}

	@Override
	public void retry() throws InterruptedException {
		if (getCurrentRetryTimes() < getMaxRetryTimes()) {
			crawlerMessage.setCurrentRetryTimes(crawlerMessage.getCurrentRetryTimes() + 1);
			createNewTask(crawlerMessage);
		} else {
			log.warn(this.getClass().getSimpleName() + "maxRetryTimes:{}, currentRetryTimes:{}", getMaxRetryTimes(), getCurrentRetryTimes());
			receiveNewTask();
		}
		isRetry = true;
	}

	protected abstract AbstractHttpClient getHttpClient();

	protected abstract String getProxyQueueName();

	protected abstract ProxyQueue getProxyQueue();

	protected abstract TaskQueueService getTaskQueueService();

	protected abstract LocalIPService getLocalIPService();

	protected boolean responseError(Page page){
		return false;
	}

}
