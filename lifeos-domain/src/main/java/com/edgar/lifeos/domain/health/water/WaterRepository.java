package com.edgar.lifeos.domain.health.water;

import java.time.Instant;
import java.util.List;

/**
 * 饮水仓储契约。
 */
public interface WaterRepository {

    WaterRecord save(WaterRecord record);

    List<WaterRecord> listByUserAndTimeRange(Long userId, Instant from, Instant to);

    /** 某用户某日累计饮水 ml。 */
    int sumByUserAndDay(Long userId, Instant dayStart, Instant dayEnd);

    WaterGoal findGoalEffectiveOn(Long userId, Instant date);
}