package com.github.wycm.zhihu;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class SimpleSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {
    static {
        System.getProperties().setProperty("env", "test");
    }
    public SimpleSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
}
