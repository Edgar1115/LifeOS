package com.edgar.lifeos.domain.planning.goal;

import java.util.List;

/**
 * Goal 仓储契约。
 */
public interface GoalRepository {

    Goal save(Goal goal);

    Goal findByIdAndUserId(Long id, Long userId);

    List<Goal> listByUser(Long userId, GoalStatus status);

    void deleteById(Long id);
}