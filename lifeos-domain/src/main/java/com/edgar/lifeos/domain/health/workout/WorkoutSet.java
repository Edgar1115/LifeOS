package com.edgar.lifeos.domain.health.workout;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;
import lombok.Getter;

/**
 * 训练组。
 */
@Getter
public class WorkoutSet extends AggregateRoot {

    private Long exerciseId;

    private int setNo;

    private BigDecimal weight;

    private Integer reps;

    private Integer durationSeconds;

    protected WorkoutSet() {
    }

    public static WorkoutSet create(Long exerciseId, int setNo, BigDecimal weight, Integer reps, Integer durationSeconds) {
        WorkoutSet s = new WorkoutSet();
        s.exerciseId = exerciseId;
        s.setNo = setNo;
        s.weight = weight;
        s.reps = reps;
        s.durationSeconds = durationSeconds;
        return s;
    }





}