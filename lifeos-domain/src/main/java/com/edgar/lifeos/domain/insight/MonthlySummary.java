package com.edgar.lifeos.domain.insight;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 月总结。
 */
@Getter
public class MonthlySummary extends AggregateRoot {

    private Long userId;

    private int year;

    private int month;

    private String statisticsJson;

    private String summaryMarkdown;

    private String suggestionsJson;

    private Instant generatedAt;

    protected MonthlySummary() {
    }

    public static MonthlySummary create(Long userId, int year, int month,
                                        String statisticsJson, String summaryMarkdown, String suggestionsJson, Instant now) {
        MonthlySummary s = new MonthlySummary();
        s.userId = userId;
        s.year = year;
        s.month = month;
        s.statisticsJson = statisticsJson;
        s.summaryMarkdown = summaryMarkdown;
        s.suggestionsJson = suggestionsJson;
        s.generatedAt = now;
        return s;
    }







}