package com.edgar.lifeos.agent.domain;

import com.edgar.lifeos.common.domain.AggregateRoot;
import lombok.Getter;

/**
 * Agent 会话。
 */
@Getter
public class AgentSession extends AggregateRoot {

    private Long userId;

    private String title;

    /** 会话状态：ACTIVE / ARCHIVED（持久化为字符串，未建模为枚举） */
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



}