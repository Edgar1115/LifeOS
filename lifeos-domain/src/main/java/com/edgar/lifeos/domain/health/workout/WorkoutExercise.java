package com.edgar.lifeos.domain.health.workout;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * 训练动作，属于某个 WorkoutSession。
 */
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

    public Long getSessionId() {
        return sessionId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public int getSortNo() {
        return sortNo;
    }
}