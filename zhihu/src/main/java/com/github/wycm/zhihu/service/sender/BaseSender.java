package com.github.wycm.zhihu.service.sender;

import com.github.wycm.common.util.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wycm on 2018/10/24.
 */
public abstract class BaseSender {
    @Autowired
    protected RedisLockUtil redisLockUtil;

    abstract void send();
}
