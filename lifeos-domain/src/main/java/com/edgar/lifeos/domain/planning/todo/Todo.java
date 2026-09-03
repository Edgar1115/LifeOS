package com.edgar.lifeos.domain.planning.todo;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Todo。
 */
public class Todo extends AggregateRoot {

    private Long userId;

    private Long goalId;

    private Long milestoneId;

    private String title;

    private String description;

    private int priority;

    private LocalDate plannedDate;

    private Instant deadline;

    private TodoStatus status;

    private Instant completedAt;

    protected Todo() {
    }

    public static Todo create(Long userId, Long goalId, Long milestoneId, String title, String description,
                              int priority, LocalDate plannedDate, Instant deadline) {
        Todo todo = new Todo();
        todo.userId = userId;
        todo.goalId = goalId;
        todo.milestoneId = milestoneId;
        todo.title = title;
        todo.description = description;
        todo.priority = priority;
        todo.plannedDate = plannedDate;
        todo.deadline = deadline;
        todo.status = TodoStatus.TODO;
        return todo;
    }

    public void complete(Instant now) {
        this.status = TodoStatus.DONE;
        this.completedAt = now;
    }

    public void cancel() {
        this.status = TodoStatus.CANCELLED;
    }

    public void start() {
        if (this.status == TodoStatus.TODO) {
            this.status = TodoStatus.DOING;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void update(String title, String description, int priority, LocalDate plannedDate, Instant deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.plannedDate = plannedDate;
        this.deadline = deadline;
    }
}