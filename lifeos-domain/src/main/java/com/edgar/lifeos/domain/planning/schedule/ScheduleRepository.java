package com.edgar.lifeos.domain.planning.schedule;

import java.time.Instant;
import java.util.List;

/**
 * Schedule 仓储契约。
 */
public interface ScheduleRepository {

    ScheduleEvent save(ScheduleEvent event);

    ScheduleEvent findByIdAndUserId(Long id, Long userId);

    List<ScheduleEvent> listByUserAndTimeRange(Long userId, Instant start, Instant end);

    void deleteById(Long id);
}