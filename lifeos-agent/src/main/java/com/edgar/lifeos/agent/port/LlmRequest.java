package com.edgar.lifeos.agent.port;

import java.util.List;

/**
 * LLM 请求。
 *
 * @param model       模型标识（如 gpt-4o、deepseek-chat）
 * @param messages    对话消息列表，按时间顺序
 * @param temperature 采样温度（0~2）：越高越随机，越低越确定；null 表示用服务端默认值
 * @param stream      是否流式返回
 */
public record LlmRequest(
        String model,
        List<LlmMessage> messages,
        Double temperature,
        boolean stream) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String model;
        private List<LlmMessage> messages;
        private Double temperature;
        private boolean stream;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<LlmMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public LlmRequest build() {
            return new LlmRequest(model, messages, temperature, stream);
        }
    }
}