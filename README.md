知乎爬虫
====  
2.0版本,和1.0版本有很大的不一样<br>
为啥会更新呢，1.0版本的代码写的比较乱，重构了下。该分支已删除<br>
暂时还没有加新功能，2.0版本运行非常简单，直接配置。<br>

##工程导入(maven)
* git clone https://github.com/wycm/zhihu-crawler 克隆项目到本地 
* **eclipse**导入步骤(eclipse_kepler版本，自带maven)，File->Import->Maven->Existing Maven Projects->选择刚刚clone的zhihu-crawler目录->导入成功
* **idea**导入步骤,File->Open->选择刚刚clone的zhihu-crawler目录->导入成功

##工程导入(不使用maven)
* eclipse或myeclipse都可以
* git clone https://github.com/wycm/zhihu-crawler 克隆项目到本地
* 创建一个名字为zhihu-new-crawler（这个随便命名）的普通工程
* 拷贝刚刚clone的zhihu-crawler/src/main/java/com目录到zhihu-new-crawler/src目录下
* 右键工程->Build Path->Add External Archives...->导入zhihu-crawler/lib下的所有jar包
* 将zhihu-crawler/src/main/resources目录下的[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)和[log4j.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/log4j.properties)拷贝至src目录下<br>
* 修改[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)的以下两个属性:

        #验证码path
        #verificationCodePath = src/zhiHuYZM.gif
        # Cookie path
        #cookiePath = src/zhihucookies
##start
1.配置 [config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties) 文件,账号密码<br>
2.Run with [Main.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/Main.java) <br>
3.首次运行，会模拟登录，需要手动输入验证码，登录成功后，会自动序列化Cookie到[resources](https://github.com/wycm/mycrawler/blob/2.0/ZhihuCrawler/src/main/resources),以后都可以不用登录。
##注意
由于知乎现在有反爬虫机制，如果访问频繁账号会封禁一段时间，不过可以通过发送邮件的方式手动解封的。
跑5个下载线程就账号可能被封。
现在默认下载线程数是2，可以通过 [config.properties](https://github.com/wycm/zhihu-crawler/tree/2.0/src/main/resources) 配置文件的`downloadThreadSize`来修改。
最好还是注册一个小号来跑吧。如果确实追求效率，那就多注册几个账号跑吧。
##TODO
支持分布式
##问题
有什么疑问，欢迎提出来

