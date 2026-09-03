package com.edgar.lifeos.domain.insight;

import java.util.List;

/**
 * Insight 仓储契约。
 */
public interface InsightRepository {

    Insight save(Insight insight);

    Insight findByIdAndUserId(Long id, Long userId);

    List<Insight> listByUser(Long userId, String status);

    /** 冷却判断：最近存在该类型未关闭的 Insight。 */
    boolean existsOpen(Long userId, InsightType type);
}