package com.github.wycm.zhihu.parser;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.zhihu.dao.mongodb.entity.TopicActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * topic 列表页
 * mongo 对应的数据
 */
@Component
public class ZhihuTopicActivityMongoPageParser implements ListPageParser<TopicActivity> {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<TopicActivity> parseListPage(Page page) {
        JSONArray jsonArray = JSONObject.parseObject(page.getHtml()).getJSONArray("data");
        List<TopicActivity> list = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++){
            try {
                TopicActivity Topic = jsonArray.getJSONObject(i).toJavaObject(TopicActivity.class);
                list.add(Topic);
            } catch (JSONException e){
                LOG.error("parse fail data: {}", JSON.toJSONString(jsonArray.getJSONObject(i)));
                LOG.error(e.getMessage(), e);
            }

        }
        return list;
    }
}
