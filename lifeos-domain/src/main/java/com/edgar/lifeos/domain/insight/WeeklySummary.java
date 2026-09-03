package com.edgar.lifeos.domain.insight;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 周总结。
 */
public class WeeklySummary extends AggregateRoot {

    private Long userId;

    private LocalDate weekStart;

    private LocalDate weekEnd;

    private String statisticsJson;

    private String summaryMarkdown;

    private String suggestionsJson;

    private Instant generatedAt;

    protected WeeklySummary() {
    }

    public static WeeklySummary create(Long userId, LocalDate weekStart, LocalDate weekEnd,
                                       String statisticsJson, String summaryMarkdown, String suggestionsJson, Instant now) {
        WeeklySummary s = new WeeklySummary();
        s.userId = userId;
        s.weekStart = weekStart;
        s.weekEnd = weekEnd;
        s.statisticsJson = statisticsJson;
        s.summaryMarkdown = summaryMarkdown;
        s.suggestionsJson = suggestionsJson;
        s.generatedAt = now;
        return s;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public String getStatisticsJson() {
        return statisticsJson;
    }

    public String getSummaryMarkdown() {
        return summaryMarkdown;
    }

    public String getSuggestionsJson() {
        return suggestionsJson;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }
}