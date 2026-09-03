package com.edgar.lifeos.common.id;

/**
 * ID 生成器接口。业务事实数据源使用 Snowflake 大整数 ID。
 */
public interface IdGenerator {

    /**
     * @return 下一个全局唯一 ID
     */
    long nextId();
}