package com.edgar.lifeos.domain.capture.diary;

import java.time.LocalDate;
import java.util.List;

/**
 * 日记仓储契约。Domain 只定义接口，实现放 infrastructure。
 */
public interface DiaryRepository {

    Diary save(Diary diary);

    /** 按主键 + 归属用户查找，防止越权（user_id 过滤）。 */
    Diary findByIdAndUserId(Long id, Long userId);

    /** 查询某用户某日期范围的日记，按日期倒序。 */
    List<Diary> findByUserAndDateRange(Long userId, LocalDate start, LocalDate end);

    /** 某用户某月存在记录的日期（用于日历视图）。 */
    List<LocalDate> findDatesByUserAndMonth(Long userId, int year, int month);

    boolean existsByUserAndDate(Long userId, LocalDate date);
}