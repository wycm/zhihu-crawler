package com.github.wycm.zhihu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.github.wycm")
public class ZhihuCrawlerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZhihuCrawlerApplication.class, args);
	}
}
