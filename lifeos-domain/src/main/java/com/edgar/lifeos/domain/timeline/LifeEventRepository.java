package com.edgar.lifeos.domain.timeline;

import java.time.Instant;
import java.util.List;

/**
 * LifeEvent 仓储契约。
 */
public interface LifeEventRepository {

    LifeEvent save(LifeEvent event);

    List<LifeEvent> listByUserAndTimeRange(Long userId, Instant start, Instant end, LifeEventType type, int limit);

    List<LifeEvent> listBySource(Long sourceType, Long sourceId);
}