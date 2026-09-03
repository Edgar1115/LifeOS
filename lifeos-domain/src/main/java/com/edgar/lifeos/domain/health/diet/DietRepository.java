package com.edgar.lifeos.domain.health.diet;

import java.time.Instant;
import java.util.List;

/**
 * Diet 仓储契约。
 */
public interface DietRepository {

    Meal save(Meal meal);

    Meal findByIdAndUserId(Long id, Long userId);

    List<Meal> listByUserAndTimeRange(Long userId, Instant from, Instant to);

    void deleteById(Long id);
}