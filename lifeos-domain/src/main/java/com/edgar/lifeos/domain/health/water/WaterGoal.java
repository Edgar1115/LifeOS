package com.edgar.lifeos.domain.health.water;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.LocalDate;

/**
 * 每日饮水目标。
 */
public class WaterGoal extends AggregateRoot {

    private Long userId;

    private int targetMl;

    private LocalDate effectiveDate;

    protected WaterGoal() {
    }

    public static WaterGoal create(Long userId, int targetMl, LocalDate effectiveDate) {
        WaterGoal g = new WaterGoal();
        g.userId = userId;
        g.targetMl = targetMl;
        g.effectiveDate = effectiveDate;
        return g;
    }

    public Long getUserId() {
        return userId;
    }

    public int getTargetMl() {
        return targetMl;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }
}