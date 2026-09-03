package com.edgar.lifeos.agent.domain;

import java.util.List;
import java.util.Optional;

/**
 * Agent 数据仓储契约（表：agent_session / agent_message / agent_run / agent_tool_call）。
 * 实现放 infrastructure。
 */
public interface AgentRepository {

    AgentSession saveSession(AgentSession session);

    Optional<AgentSession> findSessionByIdAndUserId(Long id, Long userId);

    AgentMessage saveMessage(AgentMessage message);

    List<AgentMessage> listMessages(Long sessionId, int limit);

    AgentRun saveRun(AgentRun run);

    AgentToolCall saveToolCall(AgentToolCall call);
}