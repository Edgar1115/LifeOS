package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Agent 单次工具调用记录。
 */
public class AgentToolCall extends AggregateRoot {

    private Long runId;

    private String toolName;

    private String argumentsJson;

    private String resultJson;

    private String status;

    private long latencyMs;

    protected AgentToolCall() {
    }

    public static AgentToolCall started(Long runId, String toolName, String argumentsJson) {
        AgentToolCall c = new AgentToolCall();
        c.runId = runId;
        c.toolName = toolName;
        c.argumentsJson = argumentsJson;
        c.status = "RUNNING";
        return c;
    }

    public void finish(String resultJson, String status, long latencyMs) {
        this.resultJson = resultJson;
        this.status = status;
        this.latencyMs = latencyMs;
    }

    public Long getRunId() {
        return runId;
    }

    public String getToolName() {
        return toolName;
    }

    public String getArgumentsJson() {
        return argumentsJson;
    }

    public String getResultJson() {
        return resultJson;
    }

    public String getStatus() {
        return status;
    }

    public long getLatencyMs() {
        return latencyMs;
    }
}