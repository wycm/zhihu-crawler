知乎爬虫
====  
一个简易知乎爬虫，抓取知乎用户的基本资料。<br>
刚开始爬取速度会比较慢，因为代理比较少。随着可用代理的增多，爬取速度会越来越快，最终稳定在24个用户/s左右。<br>
运行环境<br>
*-cpu:Intel(R) Core(TM) i7-4770 CPU @ 3.40GHz<br>
*-memory size: DIMM DDR3 Synchronous 1600 MHz (0.6 ns)--(8GB)<br>
*-system:ubuntu 12.04
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


##Quick Start
Run with [Main.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/Main.java) <br>

##注意
因为登录方式抓取，可能会导致封号。现在采用游客模式抓取。

##更新
####2016.12.26
* 移除未使用的包，修复ConcurrentModificationException和NoSuchElementException异常问题。
* 增加游客（免登录）模式抓取。
* 增加代理抓取模块。

####2017.01.10
* 不再采用登录抓取，并移除登录抓取相关模块，模拟登录的主要逻辑代码见[ModelLogin.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/zhihu/ModelLogin.java)。
* 优化项目结构，加快抓取速度。采用ListPageThreadPool和DetailPageThreadPool两个线程池。ListPageThreadPool负责下载”关注用户“列表页，解析出关注用户，将关注用户的url去重，然后放到DetailPageThreadPool线程池。
DetailPageThreadPool负责下载用户详情页面，解析出用户基本信息并入库，获取该用户的"关注用户"的列表页url并放到ListPageThreadPool。

##TODO
* 优化爬取速度

##最后
欢迎交流，欢迎提交代码。需要数据的可以联系我。

