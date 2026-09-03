package com.edgar.lifeos.domain.health.sleep;

import java.time.Instant;
import java.util.List;

/**
 * 睡眠仓储契约。
 */
public interface SleepRepository {

    SleepRecord save(SleepRecord record);

    SleepRecord findByIdAndUserId(Long id, Long userId);

    List<SleepRecord> listByUserAndTimeRange(Long userId, Instant from, Instant to);

    /** 连续 N 天平均时长统计所需：返回某时间段内所有记录。 */
    List<SleepRecord> listAllBetween(Long userId, Instant from, Instant to);
}