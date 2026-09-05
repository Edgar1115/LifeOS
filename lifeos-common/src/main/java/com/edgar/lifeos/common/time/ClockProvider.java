package com.edgar.lifeos.common.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间源。全局统一使用，便于测试注入固定时钟。
 */
public interface ClockProvider {

    Clock clock();

    default Instant now() {
        return Instant.now(clock());
    }

    default LocalDateTime nowLocal() {
        return LocalDateTime.now(clock());
    }

    static ClockProvider system() {
        return new ClockProvider() {
            private final Clock clock = Clock.system(SystemZone.DEFAULT_ZONE);

            @Override
            public Clock clock() {
                return clock;
            }
        };
    }

    /**
     * 系统默认时区（业务统一使用 Asia/Shanghai）。
     * 目前固定为上海时区；若未来需要按部署环境切换，可改为从配置读取。
     */
    final class SystemZone {
        private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");
    }
}