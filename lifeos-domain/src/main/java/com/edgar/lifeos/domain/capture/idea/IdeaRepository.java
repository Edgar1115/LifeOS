package com.edgar.lifeos.domain.capture.idea;

import java.util.List;

/**
 * Idea 仓储契约。
 */
public interface IdeaRepository {

    Idea save(Idea idea);

    Idea findByIdAndUserId(Long id, Long userId);

    List<Idea> listByUser(Long userId, IdeaStatus status);

    void deleteById(Long id);
}