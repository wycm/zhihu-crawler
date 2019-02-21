package com.github.wycm.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by wycm on 2019-01-26.
 */
@Component
@Data
public class CommonProperties {
    @Value("${target.page.proxy.queueName}")
    private String targetPageProxyQueueName;

    @Value("${proxy.page.proxy.queueName}")
    private String proxyPageProxyQueueName;

}
