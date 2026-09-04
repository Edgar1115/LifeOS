package com.edgar.lifeos.domain.notification;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 提醒规则。Reminder 是统一能力，不属于某个具体业务域。
 */
@Getter
public class ReminderRule extends AggregateRoot {

    private Long userId;

    private String sourceType;

    private Long sourceId;

    private ReminderTriggerType triggerType;

    private Instant triggerAt;

    private String cronExpression;

    private boolean enabled;

    protected ReminderRule() {
    }

    public static ReminderRule create(Long userId, String sourceType, Long sourceId,
                                      ReminderTriggerType triggerType, Instant triggerAt, String cronExpression) {
        ReminderRule r = new ReminderRule();
        r.userId = userId;
        r.sourceType = sourceType;
        r.sourceId = sourceId;
        r.triggerType = triggerType;
        r.triggerAt = triggerAt;
        r.cronExpression = cronExpression;
        r.enabled = true;
        return r;
    }








    public void disable() {
        this.enabled = false;
    }
}