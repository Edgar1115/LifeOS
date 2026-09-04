package com.edgar.lifeos.domain.planning.schedule;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * Schedule：独立于 Todo 的时间安排（日历事件）。
 */
@Getter
public class ScheduleEvent extends AggregateRoot {

    private Long userId;

    private String title;

    private String description;

    private Instant startTime;

    private Instant endTime;

    private String location;

    private ScheduleType eventType;

    protected ScheduleEvent() {
    }

    public static ScheduleEvent create(Long userId, String title, String description,
                                       Instant startTime, Instant endTime, String location, ScheduleType eventType) {
        ScheduleEvent s = new ScheduleEvent();
        s.userId = userId;
        s.title = title;
        s.description = description;
        s.startTime = startTime;
        s.endTime = endTime;
        s.location = location;
        s.eventType = eventType == null ? ScheduleType.OTHER : eventType;
        return s;
    }








    public void update(String title, String description, Instant startTime, Instant endTime, String location, ScheduleType eventType) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.eventType = eventType == null ? ScheduleType.OTHER : eventType;
    }
}