package com.edgar.lifeos.domain.planning.goal;

import java.util.List;

/**
 * Milestone 仓储契约。
 */
public interface MilestoneRepository {

    Milestone save(Milestone milestone);

    Milestone findByIdAndUserId(Long id, Long userId);

    List<Milestone> listByGoal(Long goalId);
}