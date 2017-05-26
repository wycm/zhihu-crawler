知乎爬虫
====
zhihu-crawler是一个基于Java的爬虫实战项目，主要功能是抓取知乎用户的基本资料，如果觉得不错，请给个star。
## 爬取结果
* 下图为爬取117w知乎用户数据的简单统计<br>
![](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/img/zhihu-charts.png)
* 详细统计见 https://www.vwycm.cn/zhihu/charts

## 工程导入(maven)
* git clone https://github.com/wycm/zhihu-crawler 克隆项目到本地
* **eclipse**导入步骤(eclipse_kepler版本，自带maven)，File->Import->Maven->Existing Maven Projects->选择刚刚clone的zhihu-crawler目录->导入成功
* **idea**导入步骤,File->Open->选择刚刚clone的zhihu-crawler目录->导入成功

## 工程导入(不使用maven)
* eclipse或myeclipse都可以
* git clone https://github.com/wycm/zhihu-crawler 克隆项目到本地
* 创建一个名字为zhihu-new-crawler（这个随便命名）的普通工程
* 拷贝刚刚clone的zhihu-crawler/src/main/java/com目录到zhihu-new-crawler/src目录下
* 右键工程->Build Path->Add External Archives...->导入zhihu-crawler/lib下的所有jar包
* 将zhihu-crawler/src/main/resources目录下的[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)和[log4j.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/log4j.properties)拷贝至src目录下

## Quick Start
Run with [Main.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/Main.java) <br>

## 使用到的API
* 地址(url)：```https://www.zhihu.com/api/v4/members/${userid}/followees```
* 请求类型：GET
* **请求参数**

| 参数名 |类型 | 必填 | 值 | 说明|
| :------------ | :------------ | :------------ | :----- | :------------ |
| include | String | 是| ```data[*]answer_count,articles_count``` |需要返回的字段（这个值可以改根据需要增加一些字段，见如下示例url） |
| offset  | int    | 是| 0 | 偏移量（通过调整这个值可以获取到一个用户的```所有关注用户```资料） |
| limit   | int    | 是| 20 | 返回用户数（最大20，超过20无效） |

* url示例：```https://www.zhihu.com/api/v4/members/wo-yan-chen-mo/followees?include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=0&limit=20```
* 响应：json数据，会有关注用户资料
* **注意**：这个请求采用了oauth验证，需要在http header加上```authorization:oauth c3cef7c66a1843f8b3a9e6a1e3160e20```,这个值是存放在js文件中，详细获取方式见代码。

## 使用到的三方库
* HttpClient4.5-网络请求
* Jsoup-html标签解析
* JsonPath-json数据解析

## Features
* 大量使用http代理，突破同一个客户端访问量限制。
* 支持持久化(mysql),相关配置见[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)。
* 多线程、爬取速度快。

## 持久化配置
* 如果要持久化数据到mysql，先手动创建数据库，然后修改配置文件[config.properties](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/config.properties)的如下属性。
* `db.enable = true`
* `db.host =ip地址`
* `db.username = 用户名`
* `db.password = 密码`
* `db.name = 数据库名`

## 流程图
![](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/resources/img/zhihu-crawler-process.jpg)
## 更新
#### 2016.12.26
* 移除未使用的包，修复ConcurrentModificationException和NoSuchElementException异常问题。
* 增加游客（免登录）模式抓取。
* 增加代理抓取模块。

#### 2017.01.10
* 不再采用登录抓取，并移除登录抓取相关模块，模拟登录的主要逻辑代码见[ModelLogin.java](https://github.com/wycm/zhihu-crawler/blob/2.0/src/main/java/com/crawl/zhihu/ModelLogin.java)。
* 优化项目结构，加快爬取速度。采用ListPageThreadPool和DetailPageThreadPool两个线程池。ListPageThreadPool负责下载”关注用户“列表页，解析出关注用户，将关注用户的url去重，然后放到DetailPageThreadPool线程池。
DetailPageThreadPool负责下载用户详情页面，解析出用户基本信息并入库，获取该用户的"关注用户"的列表页url并放到ListPageThreadPool。

#### 2017.01.17
* 增加代理序列化。
* 调整项目结构，大幅度提高爬取速度。不再使用ListPageThreadPool和DetailPageThreadPool的方式。直接下载关注列表页，可以直接获取到用户详细资料。

#### 2017.03.30
* 知乎api变更，关注列表页不能获取到关注人数，导致线程池任务不能持续下去。抓取模式切换成原来ListPageThreadPool和DetailPageThreadPool的方式。

#### 2017.05.26
* 修复代理返回错误数据，导致java.lang.reflect.UndeclaredThrowableException异常。

## TODO
* 优化爬取速度

## 最后
* 想要爬取其它数据，如问题、答案等，完全可以在此基础上自己定制。
* 有问题的请提issue。
* 欢迎贡献代码。
* 爬虫交流群：633925314，欢迎交流。

