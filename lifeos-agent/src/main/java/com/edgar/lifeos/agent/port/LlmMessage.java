package com.edgar.lifeos.agent.port;

import java.util.List;

/**
 * LLM 消息（与主流 OpenAI 兼容 API 对齐）。
 *
 * @param role    消息角色：system / user / assistant / tool
 * @param content 消息内容
 */
public record LlmMessage(String role, String content) {

    public static LlmMessage of(String role, String content) {
        return new LlmMessage(role, content);
    }
}