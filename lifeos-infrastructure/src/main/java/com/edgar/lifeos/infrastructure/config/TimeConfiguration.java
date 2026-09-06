package com.edgar.lifeos.infrastructure.config;

import com.edgar.lifeos.common.time.ClockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 时间源 Bean 配置。
 *
 * <p>将 {@link ClockProvider#system()}（System/Asia/Shanghai）注册为 Spring Bean，
 * 供需要时钟的组件（如 Repository 持久化时间戳）依赖注入。
 * 测试场景可直接替换为固定时钟的 ClockProvider 实现。</p>
 */
@Configuration
public class TimeConfiguration {

    @Bean
    public ClockProvider clockProvider() {
        return ClockProvider.system();
    }
}