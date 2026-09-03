package com.edgar.lifeos.common.domain;

import java.io.Serializable;
import java.time.Instant;

/**
 * 聚合根基类。
 *
 * <p>负责统一维护 ID、创建时间、更新时间，并收集领域事件。
 * 领域模型不应依赖 Spring/MyBatis，本类只使用 JDK 能力。
 * 并发 / 乐观锁由基础设施层在持久化时处理。</p>
 */
public abstract class AggregateRoot implements Serializable {

    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    protected AggregateRoot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}