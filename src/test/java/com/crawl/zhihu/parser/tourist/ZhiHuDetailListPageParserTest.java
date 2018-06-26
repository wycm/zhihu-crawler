package com.crawl.zhihu.parser.tourist;

import com.crawl.zhihu.ZhiHuHttpClient;
import com.crawl.zhihu.entity.Page;
import com.crawl.zhihu.entity.User;
import com.crawl.zhihu.parser.ZhiHuUserListPageParser;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class ZhiHuDetailListPageParserTest {
    @Test
    public void testParse(){
        Page page = new Page();
        HttpGet request = new HttpGet("https://www.zhihu.com/api/v4/members/wo-yan-chen-mo/followees?include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=0&limit=20");
        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getInstance().getAuthorization());
        try {
            page = ZhiHuHttpClient.getInstance().getWebPage(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<User> list = ZhiHuUserListPageParser.getInstance().parseListPage(page);
        Assert.assertTrue(list.size() > 0);
        for(User u : list){
            Assert.assertNotNull(u.getUsername());
        }
    }
}