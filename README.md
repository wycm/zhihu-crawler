基于JAVA的知乎爬虫
<p>README</p>
<p></p>
<p></p>
<p></p>
<p>爬虫入口:https://github.com/wycm/mycrawler/blob/master/ZhihuCrawler/src/main/java/com/crawl/zhihu/user/CrawlZhiHu.java</p>
<p>
1.首先模拟登录知乎，登录成功后将Cookie序列化到磁盘，不用以后每次都登录（如果不模拟登录，可以直接从浏览器塞入Cookie也是可以的）。



模拟登录通过浏览器F12抓包分析登录过程，需要注意的是验证码的获取和登录过程只能通过一个CloseHttpClient对象完成，CloseHttpClient就好
比一个浏览器。不能每次请求都创建对象，不然Cookie失效了。
</p>
<p>
2.创建两个线程池和一个Storage。一个抓取网页线程池，负责执行request请求，并返回网页内容，
存到Storage中。另一个是解析网页线程池，负责从Storage中取出网页内容并解析，解析用户资料存入数据库，
解析该用户关注的人的首页，将该地址请求又加入抓取网页线程池。一直循环下去。

</p>
<p>
3.关于url去重，
我是直接将访问过的链接md5化后存入数据库，每次访问前，查看数据库中是否存在该链接。

</p>