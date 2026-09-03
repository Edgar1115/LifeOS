package com.edgar.lifeos.agent.port;

import java.util.List;

/**
 * LLM 请求。
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