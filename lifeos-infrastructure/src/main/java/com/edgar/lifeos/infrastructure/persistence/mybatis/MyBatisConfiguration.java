package com.edgar.lifeos.infrastructure.persistence.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Mapper 扫描配置。
 *
 * <p>由于 {@code @SpringBootApplication} 位于 {@code com.edgar.lifeos.server}，
 * MyBatis 自动扫描只会基于该包查找 {@code @Mapper} 接口，扫不到
 * {@code infrastructure} 模块中的 Mapper，因此必须显式声明扫描包。</p>
 *
 * <p>{@code annotationClass = Mapper.class} 是过滤条件：只有标注 {@link Mapper} 的接口
 * 才会被注册为 Mapper；否则默认会把扫描包内的所有接口都注册为 Mapper。</p>
 */
@Configuration
@MapperScan(basePackages = "com.edgar.lifeos.infrastructure.persistence.mybatis",
        annotationClass = Mapper.class)
public class MyBatisConfiguration {
}