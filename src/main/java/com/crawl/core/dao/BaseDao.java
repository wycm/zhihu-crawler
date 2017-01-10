package com.crawl.core.dao;


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
