package com.edgar.lifeos.domain.health.workout;

import com.edgar.lifeos.common.domain.AggregateRoot;
import lombok.Getter;

/**
 * 训练动作，属于某个 WorkoutSession。
 */
@Getter
public class WorkoutExercise extends AggregateRoot {

    private Long sessionId;

    private String exerciseName;

    private String muscleGroup;

    private int sortNo;

    protected WorkoutExercise() {
    }

    public static WorkoutExercise create(Long sessionId, String exerciseName, String muscleGroup, int sortNo) {
        WorkoutExercise e = new WorkoutExercise();
        e.sessionId = sessionId;
        e.exerciseName = exerciseName;
        e.muscleGroup = muscleGroup;
        e.sortNo = sortNo;
        return e;
    }




}