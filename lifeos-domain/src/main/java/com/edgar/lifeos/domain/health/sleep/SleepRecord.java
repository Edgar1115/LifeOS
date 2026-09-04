package com.edgar.lifeos.domain.health.sleep;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 睡眠记录。
 */
@Getter
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






}