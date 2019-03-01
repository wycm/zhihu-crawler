package com.github.wycm.zhihu;

import com.github.wycm.zhihu.task.ZhihuProxyPageDownloadTask;
import com.github.wycm.zhihu.task.ZhihuTopicPageTask;


public class ZhihuConstants {

    public static final String PROXY_PAGE_REDIS_KEY_PREFIX = "Zhihu:proxy:";

    public static final String ZHIHU_PAGE_REDIS_KEY_PREFIX = "Zhihu:zhihu:";

    public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/%s/followees?" +
            "include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count," +
            "gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following," +
            "badge[?(type=best_answerer)].topics&offset=%d&limit=20";

    public final static String PROXY_PAGE_DOWNLOAD_TASK_LOCK_KEY_PREFIX = ZhihuProxyPageDownloadTask.class.getSimpleName() + "LockKey:";

    public final static String PROXY_PAGE_DOWNLOAD_TASK_QUEUE_NAME = ZhihuProxyPageDownloadTask.class.getSimpleName() + "Queue";

    public final static String TOPIC_PAGE_TASK_LOCK_KEY_PREFIX = ZhihuTopicPageTask.class.getSimpleName() + "LockKey:";

    public final static String TOPIC_PAGE_TASK_QUEUE_NAME = ZhihuTopicPageTask.class.getSimpleName() + "Queue";

//    public final static String TOPIC_ACTIVITY_PAGE_TASK_LOCK_KEY_PREFIX = TopicActivityPageTask.class.getSimpleName() + "LockKey:";
//
//    public final static String TOPIC_ACTIVITY_PAGE_TASK_QUEUE_NAME = TopicActivityPageTask.class.getSimpleName() + "Queue";
//
//    public final static String TOPIC_ACTIVITY_PAGE_TASK_PERSISTENCE_QUEUE_NAME = TopicActivityPageTask.class.getSimpleName() + "PersistenceQueue";
//
//    public final static String QUESTION_ANSWER_TASK_LOCK_KEY_PREFIX = QuestionAnswerPageTask.class.getSimpleName() + "LockKey:";
//
//    public final static String QUESTION_ANSWER_TASK_QUEUE_NAME = QuestionAnswerPageTask.class.getSimpleName() + "Queue";
//
//    public final static String QUESTION_ANSWER_TASK_PERSISTENCE_QUEUE_NAME = QuestionAnswerPageTask.class.getSimpleName() + "PersistenceQueue";


    public final static String ANSWER_URL_TEMPLATE = "https://www.zhihu.com/question/${questionId}/answer/${answerId}";

    public final static String QUESTION_URL_TEMPLATE = "https://www.zhihu.com/question/${questionId}";

    public final static String ARTICLE_URL_TEMPLATE = "https://zhuanlan.zhihu.com/p/${articleId}";

    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String ZHIHU_ROOT_TOPIC_CHILDREN_URL_TEMPLATE = "https://www.zhihu.com/api/v3/topics/${topicId}/children";
    /**
     * 话题动态url
     */
    public final static String ZHIHU_TOPIC_ACTIVITY_URL_TEMPLATE = "https://www.zhihu.com/api/v4/topics/${topicId}/feeds/timeline_activity?include=data%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.content%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Danswer%29%5D.target.is_normal%2Ccomment_count%2Cvoteup_count%2Ccontent%2Crelevant_info%2Cexcerpt.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Darticle%29%5D.target.content%2Cvoteup_count%2Ccomment_count%2Cvoting%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dtopic_sticky_module%29%5D.target.data%5B%3F%28target.type%3Dpeople%29%5D.target.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.annotation_detail%2Ccontent%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%3Bdata%5B%3F%28target.type%3Danswer%29%5D.target.author.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Darticle%29%5D.target.annotation_detail%2Ccontent%2Cauthor.badge%5B%3F%28type%3Dbest_answerer%29%5D.topics%3Bdata%5B%3F%28target.type%3Dquestion%29%5D.target.annotation_detail%2Ccomment_count&limit=20&offset=0";
    /**
     * 问题答案url
     */
    public final static String ZHIHU_ANSWER_URL_TEMPLATE = "https://www.zhihu.com/api/v4/questions/${questionId}/answers?data%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics=&data%5B%2A%5D.mark_infos%5B%2A%5D.url=&include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp&limit=${limit}&offset=${offset}&sort_by=created";
    /**
     * 问题详细detail url
     */
    public final static String ZHIHU_QUESTION_DETAIL_URL_TEMPLATE = "https://www.zhihu.com/question/${questionId}";
}
