package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Agent 消息。
 */
public class AgentMessage extends AggregateRoot {

    private Long sessionId;

    private Long userId;

    /** SYSTEM / USER / ASSISTANT / TOOL */
    private String role;

    private String content;

    protected AgentMessage() {
    }

    public static AgentMessage create(Long sessionId, Long userId, String role, String content) {
        AgentMessage m = new AgentMessage();
        m.sessionId = sessionId;
        m.userId = userId;
        m.role = role;
        m.content = content;
        return m;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}