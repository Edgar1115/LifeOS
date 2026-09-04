package com.edgar.lifeos.domain.insight;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;

/**
 * 周总结。
 */
@Getter
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







}