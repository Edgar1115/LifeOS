package com.edgar.lifeos.common.domain;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;

/**
 * 聚合根基类。
 *
 * <p>负责统一维护 ID、创建时间、更新时间，并收集领域事件。
 * 领域模型不应依赖 Spring/MyBatis，本类只使用 JDK 能力。
 * 并发 / 乐观锁由基础设施层在持久化时处理。</p>
 */
@Getter
public abstract class AggregateRoot implements Serializable {

    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    protected AggregateRoot() {
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}