package com.edgar.lifeos.domain.timeline;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * LifeEvent：系统统一的人生事件索引。
 *
 * <p>Timeline 是统一索引，不是业务事实表。业务数据变更通过 Domain Event 投影到这里。
 * 查询走 Timeline API 与 Agent Tool。</p>
 */
@Getter
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









}