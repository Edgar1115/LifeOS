package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * Agent 会话。
 */
public class AgentSession extends AggregateRoot {

    private Long userId;

    private String title;

    private String status;
    
    protected AgentSession() {
    }

    public static AgentSession create(Long userId, String title) {
        AgentSession s = new AgentSession();
        s.userId = userId;
        s.title = title;
        s.status = "ACTIVE";
        return s;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}