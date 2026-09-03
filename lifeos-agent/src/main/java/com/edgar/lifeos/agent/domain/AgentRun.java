package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;

/**
 * Agent 运行记录（一次请求 + 完整工具调用链）。
 */
public class AgentRun extends AggregateRoot {

    private Long userId;

    private Long sessionId;

    private String requestId;

    private AgentRunStatus status;

    private Instant startedAt;

    private Instant finishedAt;

    private String errorCode;

    private String errorMessage;

    protected AgentRun() {
    }

    public static AgentRun start(Long userId, Long sessionId, String requestId, Instant now) {
        AgentRun r = new AgentRun();
        r.userId = userId;
        r.sessionId = sessionId;
        r.requestId = requestId;
        r.status = AgentRunStatus.PENDING;
        r.startedAt = now;
        return r;
    }

    public void succeed(Instant now) {
        this.status = AgentRunStatus.SUCCEEDED;
        this.finishedAt = now;
    }

    public void fail(String errorCode, String errorMessage, Instant now) {
        this.status = AgentRunStatus.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.finishedAt = now;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public AgentRunStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}