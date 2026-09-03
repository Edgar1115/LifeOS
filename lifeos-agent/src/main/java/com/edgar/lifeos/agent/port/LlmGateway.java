package com.edgar.lifeos.agent.port;

/**
 * LLM 网关抽象（OpenAI Compatible / DeepSeek / Claude 等）。
 * Application 不关心具体 Provider。
 */
public interface LlmGateway {

    LlmResponse chat(LlmRequest request);

    /** 流式调用（SSE）。 */
    void chatStream(LlmRequest request, StreamCallback callback);

    interface StreamCallback {
        void onDelta(String text);

        void onDone();

        void onError(Throwable t);
    }
}