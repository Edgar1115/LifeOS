package com.edgar.lifeos.agent.port;

import java.util.List;

/**
 * LLM 响应。
 */
public record LlmResponse(String content, List<ToolCall> toolCalls, String finishReason) {

    public boolean wantsTool() {
        return toolCalls != null && !toolCalls.isEmpty();
    }

    public record ToolCall(String id, String name, String argumentsJson) {
    }
}