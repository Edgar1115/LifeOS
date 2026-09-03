package com.edgar.lifeos.common.id;

/**
 * Snowflake 风格 ID 生成器（无外部依赖版本）。
 *
 * <p>64 位布局：41 位毫秒时间戳 + 5 位机器/进程 + 5 位数据中心 + 12 位序列号。
 * 默认用 0 作为全部 workId/dataCenterId，单机可用；多实例部署时通过构造参数区分。</p>
 */
public class SnowflakeIdGenerator implements IdGenerator {

    private static final long EPOCH = 1704067200000L; // 2024-01-01T00:00:00Z

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    private final long workerId;
    private final long dataCenterId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator() {
        this(0L, 0L);
    }

    public SnowflakeIdGenerator(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("workerId 超出范围: " + workerId);
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId 超出范围: " + dataCenterId);
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    @Override
    public synchronized long nextId() {
        long timestamp = currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("时钟回拨，拒绝生成 ID");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}