package com.edgar.lifeos.agent.port;

import java.util.List;

/**
 * LLM 消息（与 API 兼容）。
 */
public record LlmMessage(String role, String content) {

    public static LlmMessage of(String role, String content) {
        return new LlmMessage(role, content);
    }
}