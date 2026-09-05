package com.edgar.lifeos.domain.identity;

/**
 * 用户状态（纯业务枚举）。
 *
 * <p>只表达领域语义，不持有任何数据库表示。数据库如何存储该枚举
 * （TINYINT 1/0）由 infrastructure 层的 {@code UserStatusTypeHandler} 负责，
 * 领域层不感知持久化细节。</p>
 */
public enum UserStatus {

    /** 正常 */
    ACTIVE,

    /** 禁用 */
    DISABLED
}
