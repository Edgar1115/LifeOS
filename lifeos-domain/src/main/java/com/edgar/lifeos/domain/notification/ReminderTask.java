package com.edgar.lifeos.domain.notification;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;

/**
 * 提醒任务：代表真正需要执行的一次提醒。
 */
public class ReminderTask extends AggregateRoot {

    private Long ruleId;

    private Long userId;

    private Instant scheduledAt;

    private String status;

    private int retryCount;

    private Instant sentAt;

    protected ReminderTask() {
    }

    public static ReminderTask create(Long ruleId, Long userId, Instant scheduledAt) {
        ReminderTask t = new ReminderTask();
        t.ruleId = ruleId;
        t.userId = userId;
        t.scheduledAt = scheduledAt;
        t.status = "PENDING";
        return t;
    }

    public void markSent(Instant now) {
        this.status = "SENT";
        this.sentAt = now;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public String getStatus() {
        return status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Instant getSentAt() {
        return sentAt;
    }
}