package com.github.wycm.zhihu.parser;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.zhihu.dao.mongodb.entity.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * topic 列表页
 * mongo 对应的数据
 */
@Component
@Slf4j
public class ZhihuTopicMongoPageParser implements ListPageParser<Topic> {

    @Override
    public List<Topic> parseListPage(Page page) {
        JSONArray userList = JSONObject.parseObject(page.getHtml()).getJSONArray("data");
        List<Topic> list = new ArrayList<>(userList.size());
        for (int i = 0; i < userList.size(); i++){
            try {
                Topic Topic = userList.getJSONObject(i).toJavaObject(Topic.class);
                list.add(Topic);
            } catch (JSONException e){
                log.error("parse fail data: {}", JSON.toJSONString(userList.getJSONObject(i)));
                log.error(e.getMessage(), e);
            }

        }
        return list;
    }
}
