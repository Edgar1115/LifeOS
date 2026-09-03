package com.edgar.lifeos.domain.health.sleep;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;

/**
 * 睡眠记录。
 */
public class SleepRecord extends AggregateRoot {

    private Long userId;

    private Instant sleepAt;

    private Instant wakeAt;

    private int durationMinutes;

    private Integer quality;

    private String note;

    protected SleepRecord() {
    }

    public static SleepRecord create(Long userId, Instant sleepAt, Instant wakeAt, Integer quality, String note) {
        SleepRecord r = new SleepRecord();
        r.userId = userId;
        r.sleepAt = sleepAt;
        r.wakeAt = wakeAt;
        r.durationMinutes = (int) java.time.Duration.between(sleepAt, wakeAt).toMinutes();
        r.quality = quality;
        r.note = note;
        return r;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getSleepAt() {
        return sleepAt;
    }

    public Instant getWakeAt() {
        return wakeAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getQuality() {
        return quality;
    }

    public String getNote() {
        return note;
    }
}