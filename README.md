知乎爬虫
====  
2.0版本,和1.0版本有很大的不一样<br>
为啥会更新呢，1.0版本的代码写的比较乱，重构了下。<br>
暂时还没有加新功能，2.0版本运行非常简单，直接配置。<br>
##运行方式
这是一个maven工程，需要maven的支持。
1.导入工程。
2.配置 [config.properties](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/resources/config.properties) 文件<br>
3.执行 [Main.java](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/java/com/crawl/Main.java) 就可以跑起来<br>
4.首次运行，会模拟登录，需要手动输入验证码，登录成功后，会自动序列化Cookie到[resources](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/resources),以后都可以不用登录。
##注意
由于知乎现在有反爬虫机制，如果访问频繁账号会封禁一段时间，不过可以通过发送邮件的方式手动解封的。
我之前跑5个下载线程就被封了。
现在默认下载线程数是3，可以通过 [config.properties](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/resources/config.properties) 配置文件的`downloadThreadSize`来修改。
最好还是注册一个小号来跑吧。如果确实追求效率，那就多注册几个账号跑吧。
##TODO
有空再改一个非maven版本的。
支持分布式
##问题
有什么疑问，欢迎提出来。

