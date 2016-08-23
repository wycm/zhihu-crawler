package com.crawl.task;

import com.crawl.dao.ConnectionManage;
import com.crawl.dao.ZhihuDAO;
import com.crawl.entity.User;
import com.crawl.util.HttpClientUtil;
import com.crawl.util.Md5Util;
import com.crawl.util.MyLogger;
import com.crawl.zhihu.client.ZhihuHttpClient;
import com.crawl.zhihu.user.GetWebPageTask;
import com.crawl.zhihu.user.MyThreadPoolExecutor;
import com.crawl.zhihu.user.Storage;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 解析网页线程，负责解析网页并将解析出的用户资料插入数据库
 */
public class ParseWebPageTask implements Runnable{
	private static Logger logger = MyLogger.getMyLogger(MyThreadPoolExecutor.class);
	public static int pwpCount = 0;//解析页面数
	public static int userCount = 0;//解析的用户数量
	Storage storage = null;
	ZhihuHttpClient zhClient = null;
	ThreadPoolExecutor gwpThreadPool = null;//获取网页线程池
	MyThreadPoolExecutor pwpThreadPool = null;//解析网页线程池
	public ParseWebPageTask(){
	}
	public ParseWebPageTask(ZhihuHttpClient zhClient
			, Storage storage
			, ThreadPoolExecutor gwpThreadPool
			, MyThreadPoolExecutor pwpThreadPool){
		this.storage = storage;
		this.zhClient = zhClient;
		this.gwpThreadPool = gwpThreadPool;
		this.pwpThreadPool = pwpThreadPool;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			pwpCount++;
			User u = null;
			String ym = storage.pop();//从任务队列里面取出一个元素
			Document doc = Jsoup.parse(ym);
			Connection cn = ConnectionManage.getConnection();
			if(doc.select("title").size() != 0){
				//解析用户信息(包含html标签，为用户页面)
				u = parseUserdetail(doc);
				if(ZhihuDAO.insetToDB(cn,u)){
//					storage.getResult().getUserVector().add(u);//将用户存入Vector
				}
				for(int i = 0;i < u.getFollowees()/20 + 1;i++){
					//获取关注用户列表,因为知乎每次最多返回20个关注用户
					String url = "https://www.zhihu.com/node/ProfileFolloweesListV2?params={%22offset%22:" + 20*i + ",%22order_by%22:%22created%22,%22hash_id%22:%22" + u.getHashId() +"%22}";
					url = url.replaceAll("[{]","%7B").replaceAll("[}]","%7D").replaceAll(" ","%20");
					if(gwpThreadPool.getQueue().size() <= 100){
						//当获取网页任务队列小于100时才获取该用户关注用户
						dealHref(cn,url);
					}
				}
			}else {
				//解析用户关注页面
				Elements es = doc.select(".zm-list-content-medium .zm-list-content-title a");
				for(Element temp:es){
					String userIndex = temp.attr("href") + "/followees";
					dealHref(cn,userIndex);
				}
			}
			if(!cn.isClosed()){
				cn.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Exception",e);
		}
	}

	/**
	 * 处理链接
	 * @param href
	 * @throws SQLException
     */
	public void dealHref(Connection cn,String href) throws SQLException {
		String md5Href = Md5Util.Convert2Md5(href);
//		if(storage.getResult().getHrefSet().add(href)){
//			if(storage.getResult().getHrefSet().size() >= 10000){
//				storage.getResult().getHrefSet().clear();
//			}
		if(ZhihuDAO.insertHref(cn,md5Href) || gwpThreadPool.getQueue().size() <= 50){
			//该链接未访问过，将用户链接插入数据库或者当前获取任务线程池没有任务（防止出现死锁）
			if(pwpThreadPool.getQueue().size() <= 100){
				//解析线程池等待任务小于100时，才加入获取任务队列
				HttpGet getRequest = null;
				try{
					getRequest = new HttpGet(href);
					//将网页中的用户关注页加入线程池等待访问
					gwpThreadPool.execute(new GetWebPageTask(zhClient,getRequest,storage,gwpThreadPool,pwpThreadPool));
				} catch(IllegalArgumentException e){
					e.printStackTrace();
					logger.error("IllegalArgumentException",e);
				}
			}
		} else{
//			logger.info(href + "--该链接已经访问过---");
		}
	}
	/**
	 * 解析个人资料
	 * @param doc
	 * @return
     */
	public User parseUserdetail(Document doc){
		User u = new User();
		u.setLocation(getUserinfo(doc,"location"));//位置
		u.setBusiness(getUserinfo(doc,"business"));//行业
		u.setEmployment(getUserinfo(doc,"employment"));//企业
		u.setPosition(getUserinfo(doc,"position"));//职位
		u.setEducation(getUserinfo(doc,"education"));//教育
		try {
			u.setUsername(doc.select(".title-section.ellipsis a").first().text());//用户名
			u.setUrl("https://www.zhihu.com" + doc.select(".title-section.ellipsis a").first().attr("href"));//用户首页链接
		} catch (NullPointerException e){
			logger.error("NullPointerException",e);
			e.printStackTrace();
		}
		u.setAgrees(Integer.valueOf(doc.select(".zm-profile-header-user-agree strong").first().text()));//赞同数
		u.setThanks(Integer.valueOf(doc.select(".zm-profile-header-user-thanks strong").first().text()));//感谢数
		u.setFollowees(Integer.valueOf(doc.select(".zm-profile-side-following strong").first().text()));//关注人数
		u.setFollowers(Integer.valueOf(doc.select(".zm-profile-side-following strong").get(1).text()));//关注者
		try {
			u.setHashId(doc.select(".zm-profile-header-op-btns.clearfix button").first().attr("data-id"));
		}catch (NullPointerException e){
			e.printStackTrace();
			//解析我的主页时，会出现空指针
			u.setHashId("843df56056dc14b8dd36ace99be09337");
		}
		return u;
	}
	/**
	 * 获取用户个人资料
	 * @param doc
	 * @param infoName
     * @return
     */
	public String getUserinfo(Document doc,String infoName){
		Element e = doc.select(".zm-profile-header-user-describe ." + infoName + ".item").first();
		if(e == null){
			return "";
		} else{
			return e.attr("title");
		}
	}

	public static void main(String args []){
		/*ParseWebPageTask p = new ParseWebPageTask();
		String s = chcUtils.readFileByBytes("d:/test/llxs.txt");
		Document doc = Jsoup.parse(s);
		logger.info(p.parseUserdetail(doc).toString());*/
		ZhihuHttpClient zh = new ZhihuHttpClient();
		String url = "https://www.zhihu.com/node/ProfileFolloweesListV2?params={%22offset%22:130,%22order_by%22:%22created%22,%22hash_id%22:%22843df56056dc14b8dd36ace99be09337%22}";
		url = url.replaceAll("[{]","%7B").replaceAll("[}]","%7D").replaceAll("[\"]","%E2%80%9C");
		HttpGet gRequest = new HttpGet(url);
//		HttpPost pRequest = new HttpPost("https://www.zhihu.com/node/ProfileFolloweesListV2");
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("params","{\"offset\":130,\"order_by\":\"created\",\"hash_id\":\"843df56056dc14b8dd36ace99be09337\"}");
//		map.put("method","next");
//		map.put("_xsrf","77a69764c4db4f05ffcbd143f8f066d6");
//		chcUtils.setHttpPostParams(pRequest,map);
		HttpClientUtil.getWebPage(zh.getHttpClient(),zh.getContext(),gRequest,"utf-8",true);
	}
}
