知乎爬虫
====  
2.0版本,和1.0版本有很大的不一样<br>
为啥会更新呢，1.0版本的代码写的比较乱，重构了下。该分支已删除<br>
暂时还没有加新功能，2.0版本运行非常简单，直接配置。<br>
##maven start
0.git clone https://github.com/wycm/zhihu-crawler 克隆项目到本地
1.这是一个maven工程，需要maven的支持<br>
2.eclipse导入步骤(我用的eclipse_kepler版本，自带maven)，File->Import->Maven->Existing Maven Projects->选择刚刚clone的zhihu-crawler目录->导入成功<br>
3.idea导入步骤,File->Open->选择刚刚clone的zhihu-crawler目录->导入成功
4.配置 [config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties) 文件,账号密码<br>
5.执行 [Main.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/Main.java) 就可以跑起来<br>
6.首次运行，会模拟登录，需要手动输入验证码，登录成功后，会自动序列化Cookie到[resources](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/resources),以后都可以不用登录。
##注意
由于知乎现在有反爬虫机制，如果访问频繁账号会封禁一段时间，不过可以通过发送邮件的方式手动解封的。
跑5个下载线程就账号可能被封。
现在默认下载线程数是2，可以通过 [config.properties](https://github.com/wycm/zhihu-crawler/tree/2.0/src/main/resources) 配置文件的`downloadThreadSize`来修改。
最好还是注册一个小号来跑吧。如果确实追求效率，那就多注册几个账号跑吧。
##TODO
有空再改一个非maven版本的。
支持分布式
##问题
有什么疑问，欢迎提出来。

