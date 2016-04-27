package com.crawl.zhihu.crawlimganswer;

import com.crawl.util.MyLogger;
import com.crawl.entity.Href;
import org.apache.log4j.Logger;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者消费者模型仓库
 * @author Administrator
 *
 */
public class ImgAnswerStorage {
	private static Logger logger = MyLogger.getMyLogger(ImgAnswerStorage.class);
	private BlockingQueue<Href> queue = null;
	private Vector<Href> result = null;
	public ImgAnswerStorage(){
		queue = new LinkedBlockingQueue<Href>(10000);
		result = new Vector<Href>();
	}
	public BlockingQueue<Href> getQueue() {
		return queue;
	}

	public Vector<Href> getResult() {
		return result;
	}

	public void setResult(Vector<Href> result) {
		this.result = result;
	}

	public void setQueue(BlockingQueue<Href> queue) {
		this.queue = queue;
	}

	/**
	 * 入队
	 * 该方法阻塞
	 * @param s
	 * @throws InterruptedException
	 */
	public void push(Href s){
		try {
			queue.put(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("InterruptedException",e);
		}
		System.out.println("入队成功--当前仓库元素个数" + queue.size());
	}
	/**
	 * 出队
	 * 该方法阻塞
	 * @return
	 * @throws InterruptedException
	 */
	public Href pop(){
		Href s = null;
		try {
			s = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("InterruptedException",e);
		}
		System.out.println("出队成功--当前仓库元素个数" + queue.size());
		return s;

	}
}
