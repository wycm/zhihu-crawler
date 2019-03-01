package com.github.wycm.zhihu.service.sender;

import com.github.wycm.common.util.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseSender {
    @Autowired
    protected RedisLockUtil redisLockUtil;

    abstract void send();
}
