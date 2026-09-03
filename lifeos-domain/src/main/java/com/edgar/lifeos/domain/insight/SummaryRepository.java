package com.edgar.lifeos.domain.insight;

import java.time.LocalDate;

/**
 * Weekly/Monthly Summary 仓储契约。
 */
public interface SummaryRepository {

    WeeklySummary saveWeekly(WeeklySummary summary);

    WeeklySummary findWeekly(Long userId, LocalDate weekStart);

    WeeklySummary findLatestWeekly(Long userId);

    MonthlySummary saveMonthly(MonthlySummary summary);

    MonthlySummary findMonthly(Long userId, int year, int month);
}