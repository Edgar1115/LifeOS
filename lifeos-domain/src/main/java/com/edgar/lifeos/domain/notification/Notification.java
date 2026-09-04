package com.edgar.lifeos.domain.notification;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 通知（Inbox 条目）。单独留存判断记录。
 */
@Getter
public class Notification extends AggregateRoot {

    private Long userId;

    private String type;

    private String severity;

    private String title;

    private String content;

    private String relatedType;

    private Long relatedId;

    private boolean readStatus;

    private Instant createTime;

    protected Notification() {
    }

    public static Notification create(Long userId, String type, String severity, String title,
                                      String content, String relatedType, Long relatedId, Instant now) {
        Notification n = new Notification();
        n.userId = userId;
        n.type = type;
        n.severity = severity;
        n.title = title;
        n.content = content;
        n.relatedType = relatedType;
        n.relatedId = relatedId;
        n.readStatus = false;
        n.createTime = now;
        return n;
    }

    public void markRead() {
        this.readStatus = true;
    }









}