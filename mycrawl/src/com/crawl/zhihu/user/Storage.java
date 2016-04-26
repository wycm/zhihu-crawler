package com.crawl.zhihu.user;

import com.crawl.util.MyLogger;
import com.crawl.zhihu.client.Result;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者消费者模型仓库
 * @author Administrator
 *
 */
public class Storage {
	private static Logger logger = MyLogger.getMyLogger(Storage.class);
	private BlockingQueue<String> queue = null;
	private Result result = null;
	public Storage(){
		queue = new LinkedBlockingQueue<String>();
		result = new Result();
	}
	public BlockingQueue<String> getQueue() {
		return queue;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	/**
	 * 入队
	 * 该方法阻塞
	 * @param s
	 * @throws InterruptedException
	 */
	public void push(String s){
		try {
			queue.put(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("InterruptedException",e);
		}
		logger.info("入队成功--当前仓库元素个数" + queue.size());
	}
	/**
	 * 出队
	 * 该方法阻塞
	 * @return
	 * @throws InterruptedException
	 */
	public String pop(){
		String s = null;
		try {
			s = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("InterruptedException",e);
		}
		logger.info("出队成功--当前仓库元素个数" + queue.size());
		return s;

	}
}
