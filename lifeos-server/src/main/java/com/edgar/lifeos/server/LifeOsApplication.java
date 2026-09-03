package com.edgar.lifeos.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * LifeOS 启动入口（v2.0 模块化单体）。
 *
 * <p>扫描 {@code com.edgar.lifeos} 下的所有模块组件。</p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.edgar.lifeos")
public class LifeOsApplication {

    private static final Logger log = LoggerFactory.getLogger(LifeOsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LifeOsApplication.class, args);
        log.info("LifeOS 启动完成");
    }
}