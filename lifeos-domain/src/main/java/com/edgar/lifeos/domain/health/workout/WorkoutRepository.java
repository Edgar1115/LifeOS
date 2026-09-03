package com.edgar.lifeos.domain.health.workout;

import java.time.Instant;
import java.util.List;

/**
 * Workout 仓储契约。
 */
public interface WorkoutRepository {

    WorkoutSession save(WorkoutSession session);

    WorkoutSession findByIdAndUserId(Long id, Long userId);

    List<WorkoutSession> listByUserAndTimeRange(Long userId, Instant from, Instant to);

    List<WorkoutSession> listRecent(Long userId, int limit);
}