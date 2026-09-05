package com.edgar.lifeos.agent.domain;

/**
 * Agent 运行状态机：PENDING → RUNNING → SUCCEEDED / FAILED。
 */
public enum AgentRunStatus {
    PENDING,
    RUNNING,
    SUCCEEDED,
    FAILED
}