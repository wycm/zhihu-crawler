知乎爬虫
====  
一个简易知乎爬虫，抓取知乎用户的基本资料。<br>

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
* 将zhihu-crawler/src/main/resources目录下的[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)和[log4j.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/log4j.properties)拷贝至src目录下
* 修改[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)的以下两个属性:

        #验证码path
        #verificationCodePath = src/zhiHuYZM.gif
        # Cookie path
        #cookiePath = src/zhihucookies

##Quick Start
Run with [Main.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/Main.java) <br>

##注意
因为登录方式抓取，可能会导致封号。现在默认使用游客模式抓取。

##更新
####2016.12.26
* 移除未使用的包，修复ConcurrentModificationException和NoSuchElementException异常问题。
* 增加游客(免登录)模式抓取。
* 添加代理抓取。

##最后
欢迎交流，欢迎提交代码

