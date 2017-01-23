知乎爬虫
====  
知乎爬虫，主抓取知乎用户的基本资料。<br>
第一次爬取时，刚开始爬取速度会比较慢，因为代理比较少。随着可用代理的增多，爬取速度会越来越快。可以达到100+用户/s<br>
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

##Features
* 大量使用http代理，突破同一个客户端访问量限制。
* 支持持久化(mysql),相关配置见[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)。
* 多线程、快速，0.5小时可爬取20w用户。

##更新
####2016.12.26
* 移除未使用的包，修复ConcurrentModificationException和NoSuchElementException异常问题。
* 增加游客（免登录）模式抓取。
* 增加代理抓取模块。

####2017.01.10
* 不再采用登录抓取，并移除登录抓取相关模块，模拟登录的主要逻辑代码见[ModelLogin.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/zhihu/ModelLogin.java)。
* 优化项目结构，加快爬取速度。采用ListPageThreadPool和DetailPageThreadPool两个线程池。ListPageThreadPool负责下载”关注用户“列表页，解析出关注用户，将关注用户的url去重，然后放到DetailPageThreadPool线程池。
DetailPageThreadPool负责下载用户详情页面，解析出用户基本信息并入库，获取该用户的"关注用户"的列表页url并放到ListPageThreadPool。

####2017.01.17
* 增加代理序列化。
* 调整项目结构，大幅度提高爬取速度。不再使用ListPageThreadPool和DetailPageThreadPool的方式。直接下载关注列表页，可以直接获取到用户详细资料。

##TODO
* 优化爬取速度

##最后
* 想要爬取其它数据，如问题、答案等，完全可以在此基础上自己定制。
* 有疑问想法的，欢迎交流。
* 需要数据的可以联系我，邮箱`1057160387@qq.com`。

