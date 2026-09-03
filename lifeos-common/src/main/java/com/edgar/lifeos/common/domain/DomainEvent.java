package com.edgar.lifeos.common.domain;

/**
 * 领域事件基类。
 *
 * <p>同一事务内写入 Outbox 表，由 Dispatcher 异步投递给各个 Handler。
 * 只携带 ID、时间、事件类型与必要字段，不允许放业务 Service。</p>
 */
public interface DomainEvent {

    /** 事件唯一 ID，用于 Outbox 幂等。 */
    String eventId();

    /** 事件发生时间（epoch millis，统一由 ClockProvider 提供）。 */
    long occurredAt();

    /**
     * 事件类型名，例如 {@code "TodoCompletedEvent"}。
     * 默认取类名，方便序列化与日志检索。
     */
    default String eventType() {
        return getClass().getSimpleName();
    }
}