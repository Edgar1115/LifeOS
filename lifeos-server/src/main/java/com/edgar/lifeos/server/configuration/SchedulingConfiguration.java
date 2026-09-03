package com.edgar.lifeos.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 异步与调度能力配置。
 * Schedule 用于 Outbox Dispatcher、Reminder Scheduler 等后台任务。
 */
@Configuration
@EnableAsync
@EnableScheduling
public class SchedulingConfiguration {
}