package com.edgar.lifeos.domain.planning.goal;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Milestone：目标的里程碑。
 */
public class Milestone extends AggregateRoot {

    private Long userId;

    private Long goalId;

    private String title;

    private LocalDate deadline;

    private BigDecimal progress;

    private MilestoneStatus status;

    protected Milestone() {
    }

    public static Milestone create(Long userId, Long goalId, String title, LocalDate deadline) {
        Milestone m = new Milestone();
        m.userId = userId;
        m.goalId = goalId;
        m.title = title;
        m.deadline = deadline;
        m.progress = BigDecimal.ZERO;
        m.status = MilestoneStatus.ACTIVE;
        return m;
    }

    public void updateProgress(BigDecimal progress) {
        this.progress = progress;
        this.status = progress.compareTo(BigDecimal.ZERO) == 0 ? MilestoneStatus.ACTIVE
                : progress.compareTo(new BigDecimal("100")) >= 0 ? MilestoneStatus.COMPLETED
                : MilestoneStatus.ACTIVE;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public MilestoneStatus getStatus() {
        return status;
    }
}