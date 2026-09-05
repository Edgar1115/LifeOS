package com.edgar.lifeos.infrastructure.persistence.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 配置。
 *
 * <p>扫描 Mapper 接口所在包，使其注册为 Spring Bean；
 * XML 映射文件位置在 {@code application.yml} 的 {@code mybatis.mapper-locations} 中配置。</p>
 */
@Configuration
@MapperScan("com.edgar.lifeos.infrastructure.persistence.mybatis")
public class MyBatisConfiguration {
}