package com.edgar.lifeos.agent.tool;

/**
 * Agent 工具契约。Agent 只认识 Tool，不接触业务 Mapper/SQL。
 * 实现方位于 lifeos-infrastructure（agent tool 适配器）。
 */
public interface AgentTool {

    /** 工具名，如 "search_notes"、"create_todo"。 */
    String name();

    /** 工具描述（供 LLM 选择）。 */
    String description();

    /** READ_ONLY 可自动执行；MUTATING 需权限/幂等/审计。 */
    ToolPermission permission();

    /** 是否需要用户确认。 */
    default boolean requiresConfirmation() {
        return false;
    }

    /**
     * 执行工具。
     *
     * @param request  统一的工具请求（含 userId 和参数 JSON）
     * @return 工具执行结果（转为 JSON 回填给 LLM）
     */
    ToolResult execute(ToolRequest request);
}