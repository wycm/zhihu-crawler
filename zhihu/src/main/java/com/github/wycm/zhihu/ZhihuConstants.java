package com.github.wycm.zhihu;

/**
 * Created by wycm on 2019-01-26.
 */
public class ZhihuConstants {

    public static final String PROXY_PAGE_REDIS_KEY_PREFIX = "Zhihu:proxy:";

    public static final String ZHIHU_PAGE_REDIS_KEY_PREFIX = "Zhihu:zhihu:";

    public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/%s/followees?" +
            "include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count," +
            "gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following," +
            "badge[?(type=best_answerer)].topics&offset=%d&limit=20";
}
