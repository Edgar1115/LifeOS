package com.edgar.lifeos.agent.tool;

/**
 * 工具执行结果。
 *
 * @param ok      是否成功
 * @param dataJson 结果 JSON（成功时），直接回填给 LLM
 * @param error    失败信息
 */
public record ToolResult(boolean ok, String dataJson, String error) {

    public static ToolResult success(Object data) {
        return new ToolResult(true, toJson(data), null);
    }

    public static ToolResult failure(String error) {
        return new ToolResult(false, null, error);
    }

    private static String toJson(Object data) {
        return com.edgar.lifeos.common.json.JsonUtils.toJson(data);
    }
}