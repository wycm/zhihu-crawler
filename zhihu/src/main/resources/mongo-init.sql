use zhihu

db.createCollection("CrawledUrl")
db.CrawledUrl.remove({})
db.CrawledUrl.createIndex({"url":1},{"unique":true})

db.createCollection("ZhihuTopic")
db.ZhihuTopic.remove({})
db.ZhihuTopic.createIndex({"url":1},{"unique":true})
db.ZhihuTopic.createIndex({"topicActivityUpdateTime":1}, {background: true})
db.ZhihuTopic.insert({"is_black": false, "name": "\u300c\u6839\u8bdd\u9898\u300d", "url": "http://www.zhihu.com/api/v3/topics/19776749", "excerpt": "\u77e5\u4e4e\u7684\u5168\u90e8\u8bdd\u9898\u901a\u8fc7\u7236\u5b50\u5173\u7cfb\u6784\u6210\u4e00\u4e2a\u6709\u6839\u65e0\u5faa\u73af\u7684\u6709\u5411\u56fe \u3002 \u300c\u6839\u8bdd\u9898\u300d\u5373\u4e3a\u6240\u6709\u8bdd\u9898\u7684\u6700\u4e0a\u5c42\u7684\u7236\u8bdd\u9898\u3002 \u8bdd\u9898\u7cbe\u534e \u5373\u4e3a\u77e5\u4e4e\u7684 Top1000 \u9ad8\u7968\u56de\u7b54\u3002 \u8bf7\u4e0d\u8981\u5728\u95ee\u9898\u4e0a\u76f4\u63a5\u7ed1\u5b9a\u300c\u6839\u8bdd\u9898\u300d\u3002 \u8fd9\u6837\u4f1a\u4f7f\u95ee\u9898\u8bdd\u9898\u8fc7\u4e8e\u5bbd\u6cdb\u3002", "introduction": "\u77e5\u4e4e\u7684\u5168\u90e8\u8bdd\u9898\u901a\u8fc7\u7236\u5b50\u5173\u7cfb\u6784\u6210\u4e00\u4e2a<a href=\"http://www.zhihu.com/question/21544822\" data-editable=\"true\" data-title=\"\u4ec0\u4e48\u662f\u5408\u7406\u7684\u8bdd\u9898\u7ed3\u6784\uff1f\"><b>\u6709\u6839\u65e0\u5faa\u73af\u7684\u6709\u5411\u56fe<\/b><\/a>\u3002<br>\u300c\u6839\u8bdd\u9898\u300d\u5373\u4e3a\u6240\u6709\u8bdd\u9898\u7684\u6700\u4e0a\u5c42\u7684\u7236\u8bdd\u9898\u3002<br><a href=\"http://www.zhihu.com/topic/19776749/top-answers\" data-editable=\"true\" data-title=\"\u8bdd\u9898\u7cbe\u534e\" class=\"\">\u8bdd\u9898\u7cbe\u534e<\/a>\u5373\u4e3a\u77e5\u4e4e\u7684 Top1000 \u9ad8\u7968\u56de\u7b54\u3002<br>\u8bf7\u4e0d\u8981\u5728\u95ee\u9898\u4e0a\u76f4\u63a5\u7ed1\u5b9a\u300c\u6839\u8bdd\u9898\u300d\u3002<br>\u8fd9\u6837\u4f1a\u4f7f\u95ee\u9898\u8bdd\u9898\u8fc7\u4e8e\u5bbd\u6cdb\u3002", "avatar_url": "https://pic1.zhimg.com/50/7d3842057_qhd.jpg", "type": "topic", "_id": "19776749"})

db.createCollection("TopicActivity")
db.TopicActivity.remove({})
db.TopicActivity.createIndex({"attached_info":1},{"unique":true})

db.createCollection("ZhihuQuestion")
db.ZhihuQuestion.remove({})
db.ZhihuQuestion.createIndex({"url":1},{"unique":true})
db.ZhihuQuestion.createIndex({"answerUpdateTime":1}, {background: true})



db.createCollection("ZhihuUser")
db.ZhihuUser.remove({})
