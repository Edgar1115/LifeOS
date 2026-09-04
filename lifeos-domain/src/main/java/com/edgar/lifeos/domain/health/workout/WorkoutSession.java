package com.edgar.lifeos.domain.health.workout;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 一次训练会话。
 */
@Getter
public class WorkoutSession extends AggregateRoot {

    private Long userId;

    private String title;

    private Instant startTime;

    private Instant endTime;

    private Integer durationMinutes;

    private String note;

    protected WorkoutSession() {
    }

    public static WorkoutSession create(Long userId, String title, Instant startTime) {
        WorkoutSession s = new WorkoutSession();
        s.userId = userId;
        s.title = title;
        s.startTime = startTime;
        return s;
    }

    public void finish(Instant endTime, String note) {
        this.endTime = endTime;
        this.note = note;
        this.durationMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }






}