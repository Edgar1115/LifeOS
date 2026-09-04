package com.edgar.lifeos.domain.health.water;

import com.edgar.lifeos.common.domain.AggregateRoot;

import java.time.Instant;
import lombok.Getter;

/**
 * 饮水记录。
 */
@Getter
public class WaterRecord extends AggregateRoot {

    private Long userId;

    private int amountMl;

    private Instant recordedAt;

    /** 来源：MANUAL / AGENT / WEIXIN 等 */
    private String source;

    protected WaterRecord() {
    }

    public static WaterRecord create(Long userId, int amountMl, Instant recordedAt, String source) {
        WaterRecord r = new WaterRecord();
        r.userId = userId;
        r.amountMl = amountMl;
        r.recordedAt = recordedAt;
        r.source = source;
        return r;
    }




}