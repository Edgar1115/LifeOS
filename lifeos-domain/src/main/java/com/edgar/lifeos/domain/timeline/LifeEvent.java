package com.edgar.lifeos.domain.timeline;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;

/**
 * LifeEvent：系统统一的人生事件索引。
 *
 * <p>Timeline 是统一索引，不是业务事实表。业务数据变更通过 Domain Event 投影到这里。
 * 查询走 Timeline API 与 Agent Tool。</p>
 */
public class LifeEvent extends AggregateRoot {

    private Long userId;

    private LifeEventType eventType;

    /** 来源类型，如 WORKOUT、DIARY、TODO */
    private String sourceType;

    private Long sourceId;

    private String title;

    private String summary;

    private Instant startTime;

    private Instant endTime;

    private String metadataJson;

    protected LifeEvent() {
    }

    public static LifeEvent create(Long userId, LifeEventType eventType, String sourceType, Long sourceId,
                                   String title, String summary, Instant startTime, Instant endTime, String metadataJson) {
        LifeEvent e = new LifeEvent();
        e.userId = userId;
        e.eventType = eventType;
        e.sourceType = sourceType;
        e.sourceId = sourceId;
        e.title = title;
        e.summary = summary;
        e.startTime = startTime;
        e.endTime = endTime;
        e.metadataJson = metadataJson;
        return e;
    }

    public Long getUserId() {
        return userId;
    }

    public LifeEventType getEventType() {
        return eventType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public String getMetadataJson() {
        return metadataJson;
    }
}