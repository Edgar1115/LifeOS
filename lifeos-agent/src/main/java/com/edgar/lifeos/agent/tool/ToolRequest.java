package com.edgar.lifeos.agent.tool;

/**
 * 工具请求：携带目标用户在语境中的所有参数。
 */
public record ToolRequest(Long userId, String argumentsJson) {

    public static ToolRequest of(Long userId, String argumentsJson) {
        return new ToolRequest(userId, argumentsJson);
    }
}