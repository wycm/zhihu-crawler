package com.crawl.zhihu;

import com.crawl.entity.Page;
import com.crawl.util.MyLogger;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存放网页 仓库
 * @author wy
 *
 */
public class Storage {
	private static Logger logger = MyLogger.getMyLogger(Storage.class);
	private BlockingQueue<Page> queue = null;//阻塞队列，存放网页内容
	private Result result = null;
	public Storage(){
		queue = new LinkedBlockingQueue<Page>();
		result = new Result();
	}
	public BlockingQueue<Page> getQueue() {
		return queue;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public void setQueue(BlockingQueue<Page> queue) {
		this.queue = queue;
	}

	/**
	 * 入队
	 * 该方法阻塞
	 * @param s
	 * @throws InterruptedException
	 */
	public void push(Page s){
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
	public Page pop(){
		Page s = null;
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
