package com.edgar.lifeos.application.event;

import com.edgar.lifeos.common.domain.DomainEvent;

/**
 * 领域事件发布端口。所有业务 Service 通过它发布事件，
 * 实现（Infrastructure Outbox）在同一事务内落库，确保可靠投递。
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}