package com.crawl.zhihu.task;

import com.crawl.core.parser.DetailPageParser;
import com.crawl.core.util.HttpClientUtil;
import com.crawl.core.util.SimpleInvocationHandler;
import com.crawl.proxy.ProxyPool;
import com.crawl.proxy.entity.Direct;
import com.crawl.proxy.entity.Proxy;
import com.crawl.zhihu.dao.ZhiHuDao1;
import com.crawl.zhihu.dao.ZhiHuDao1Imp;
import com.crawl.zhihu.entity.Page;
import com.crawl.core.util.SimpleLogger;
import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.parser.ZhiHuNewUserDetailPageParser;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

import static com.crawl.core.util.Constants.TIME_INTERVAL;

/**
 * 下载网页任务， 下载成功的Page放到解析线程池
 * 若使用代理，从ProxyPool中取
 * @see ProxyPool
 */
public abstract class AbstractPageTask implements Runnable{
	private static Logger logger = SimpleLogger.getSimpleLogger(AbstractPageTask.class);
	protected String url;
	protected HttpRequestBase request;
	private boolean proxyFlag;//是否通过代理下载
	private Proxy currentProxy;//当前线程使用的代理
	protected static ZhiHuDao1 zhiHuDao1;
	protected static ZhiHuHttpClient zhiHuHttpClient = ZhiHuHttpClient.getInstance();
	static {
		zhiHuDao1 = getZhiHuDao1();
	}
	public AbstractPageTask(){

	}
	public AbstractPageTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}
	public AbstractPageTask(HttpRequestBase request, boolean proxyFlag){
		this.request = request;
		this.proxyFlag = proxyFlag;
	}
	public void run(){
		HttpGet tempRequest = null;
		try {
			Page page = null;
			if(url != null){
				if (proxyFlag){
					tempRequest = new HttpGet(url);
					currentProxy = ProxyPool.proxyQueue.take();
					if(!(currentProxy instanceof Direct)){
						HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
						tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
					}
					page = zhiHuHttpClient.getWebPage(tempRequest);
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
			page.setProxy(currentProxy);
			int status = page.getStatusCode();
			if(status == HttpStatus.SC_OK){
				if (page.getHtml().contains("zhihu")){
					logger.debug(Thread.currentThread().getName() + " " + getProxyStr(currentProxy)  + " statusCode:" + status + "  executing request " + page.getUrl());
					currentProxy.setSuccessfulTimes(currentProxy.getSuccessfulTimes() + 1);
					handle(page);
				}else {
					/**
					 * 代理异常，没有正确返回目标url
					 */
					logger.warn("proxy exception:" + currentProxy.toString());
				}

			}
			/**
			 * 401--不能通过验证
			 */
			else if(status == 404 || status == 401 ||
					status == 410){
				logger.warn(Thread.currentThread().getName() + " " + getProxyStr(currentProxy)  + " statusCode:" + status + "  executing request " + page.getUrl());
			}
			else {
				logger.error(Thread.currentThread().getName() + " " + getProxyStr(currentProxy)  + " statusCode:" + status + "  executing request " + page.getUrl());
				Thread.sleep(100);
				retry();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		} catch (IOException e) {
            if(currentProxy != null){
                /**
                 * 该代理可用，将该代理继续添加到proxyQueue
                 */
                currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
            }
            if(!zhiHuHttpClient.getDetailPageThreadPool().isShutdown()){
				retry();
			}
		} finally {
			if (request != null){
				request.releaseConnection();
			}
			if (tempRequest != null){
				tempRequest.releaseConnection();
			}
			setProxyUseStrategy();
		}
	}

	/**
	 * retry
	 */
	abstract void retry();

    /**
     * 是否继续使用代理
	 * 失败次数大于３，且失败率超过60%，则丢弃
     */
	private void setProxyUseStrategy(){
        if (currentProxy != null){
            int succTimes = currentProxy.getSuccessfulTimes();
            int failTimes = currentProxy.getFailureTimes();
            if(failTimes >= 3){
                double failRate = (failTimes + 0.0) / (succTimes + failTimes);
                if (failRate > 0.6){
                    return;
                }
            }
            currentProxy.setTimeInterval(TIME_INTERVAL);
            ProxyPool.proxyQueue.add(currentProxy);//将当前代理放入代理池中
        }
    }

	abstract void handle(Page page);

	private String getProxyStr(Proxy proxy){
		if (proxy == null){
			return "";
		}
		return proxy.getIp() + ":" + proxy.getPort();
	}
	/**
	 * 代理类，统计方法执行时间
	 * @return
	 */
	private static ZhiHuDao1 getZhiHuDao1(){
		ZhiHuDao1 zhiHuDao1 = new ZhiHuDao1Imp();
		InvocationHandler invocationHandler = new SimpleInvocationHandler(zhiHuDao1);
		ZhiHuDao1 proxyZhiHuDao1 = (ZhiHuDao1) java.lang.reflect.Proxy.newProxyInstance(zhiHuDao1.getClass().getClassLoader(),
				zhiHuDao1.getClass().getInterfaces(), invocationHandler);
		return proxyZhiHuDao1;
	}
}
