package com.edgar.lifeos.domain.notification;

import java.util.List;
import java.util.Optional;

/**
 * 通知/提醒仓储契约。
 */
public interface NotificationRepository {

    ReminderRule saveRule(ReminderRule rule);

    Optional<ReminderRule> findRuleByIdAndUserId(Long id, Long userId);

    List<ReminderRule> listDueRules(java.time.Instant now);

    ReminderTask saveTask(ReminderTask task);

    List<ReminderTask> listPendingTasks(java.time.Instant maxScheduledAt);

    Notification saveNotification(Notification notification);

    List<Notification> listByUser(Long userId);
}