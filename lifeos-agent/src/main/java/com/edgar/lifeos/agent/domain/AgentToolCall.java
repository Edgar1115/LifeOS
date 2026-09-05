package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;
import lombok.Getter;

/**
 * Agent 单次工具调用记录。
 */
@Getter
public class AgentToolCall extends AggregateRoot {

    private Long runId;

    private String toolName;

    private String argumentsJson;

    private String resultJson;

    /** 调用状态：RUNNING / SUCCEEDED / FAILED（持久化为字符串） */
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






}