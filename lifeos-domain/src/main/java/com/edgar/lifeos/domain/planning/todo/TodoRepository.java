package com.edgar.lifeos.domain.planning.todo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Todo 仓储契约。
 */
public interface TodoRepository {

    Todo save(Todo todo);

    Todo findByIdAndUserId(Long id, Long userId);

    List<Todo> listByUser(Long userId, LocalDate plannedDate, TodoStatus status);

    /** 待办完成率统计用：某用户某时间段内所有已完成 Todo。 */
    long countCompletedBetween(Long userId, Instant from, Instant to);

    long countAllBetween(Long userId, Instant from, Instant to);
}