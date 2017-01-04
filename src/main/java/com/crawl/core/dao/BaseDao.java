package com.crawl.core.dao;

/**
 * @ClassName: BaseDao
 * @Description: 插入数据库的底层baseDao
 * @author: 杨爽
 * @date: 2016/12/16 9:35
 */
public interface BaseDao<T> {


    /**
     * 插入数据
     *
     * @param t
     */
    void insert(T t);

    /**
     * 删除数据
     *
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 是否已存在
     *
     * @param userId
     * @return
     */
    boolean isContains(String userId);


}
