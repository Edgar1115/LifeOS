package com.edgar.lifeos.domain.insight;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;

/**
 * Insight：规则引擎产生的洞察/提醒。
 */
public class Insight extends AggregateRoot {

    private Long userId;

    private InsightType type;

    private InsightSeverity severity;

    private String title;

    private String description;

    private String evidenceJson;

    private String status;

    private Instant detectedAt;

    private Instant expiresAt;

    protected Insight() {
    }

    public static Insight create(Long userId, InsightType type, InsightSeverity severity,
                                 String title, String description, String evidenceJson, Instant detectedAt, Instant expiresAt) {
        Insight i = new Insight();
        i.userId = userId;
        i.type = type;
        i.severity = severity;
        i.title = title;
        i.description = description;
        i.evidenceJson = evidenceJson;
        i.status = "OPEN";
        i.detectedAt = detectedAt;
        i.expiresAt = expiresAt;
        return i;
    }

    public void dismiss() {
        this.status = "DISMISSED";
    }

    public Long getUserId() {
        return userId;
    }

    public InsightType getType() {
        return type;
    }

    public InsightSeverity getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEvidenceJson() {
        return evidenceJson;
    }

    public String getStatus() {
        return status;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}