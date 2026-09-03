package com.edgar.lifeos.domain.planning.goal;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Goal：目标。
 */
public class Goal extends AggregateRoot {

    private Long userId;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate deadline;

    /** 进度 0-100 */
    private BigDecimal progress;

    /** 优先级 1-5，默认 3 */
    private int priority;

    private GoalStatus status;

    protected Goal() {
    }

    public static Goal create(Long userId, String title, String description, LocalDate startDate, LocalDate deadline, int priority) {
        Goal goal = new Goal();
        goal.userId = userId;
        goal.title = title;
        goal.description = description;
        goal.startDate = startDate;
        goal.deadline = deadline;
        goal.progress = BigDecimal.ZERO;
        goal.priority = priority;
        goal.status = GoalStatus.ACTIVE;
        return goal;
    }

    public void updateProgress(BigDecimal newProgress) {
        if (newProgress == null || newProgress.compareTo(BigDecimal.ZERO) < 0 || newProgress.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("进度必须位于 0-100");
        }
        this.progress = newProgress;
        if (newProgress.compareTo(BigDecimal.ZERO) == 0) {
            this.status = GoalStatus.ACTIVE;
        } else if (newProgress.compareTo(new BigDecimal("100")) == 0) {
            this.status = GoalStatus.COMPLETED;
        }
    }

    public void complete() {
        this.progress = new BigDecimal("100");
        this.status = GoalStatus.COMPLETED;
    }

    public void archive() {
        this.status = GoalStatus.ARCHIVED;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public int getPriority() {
        return priority;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void update(String title, String description, LocalDate startDate, LocalDate deadline, int priority) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.priority = priority;
    }
}