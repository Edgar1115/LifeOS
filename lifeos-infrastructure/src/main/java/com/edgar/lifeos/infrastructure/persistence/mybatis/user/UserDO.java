package com.edgar.lifeos.infrastructure.persistence.mybatis.user;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户表（life_user）数据库对象。
 *
 * <p>DO 与领域模型隔离：字段为数据库原始表示，
 * {@code status} 存整数 dbValue（1=ACTIVE，0=DISABLED），
 * 转换由 Repository 实现负责。</p>
 */
@Getter
@Setter
public class UserDO implements Serializable {

    private Long id;

    /** 微信 openid，同一公众号/小程序下唯一 */
    private String openid;

    /** 微信 unionid（跨公众号/小程序唯一，可空） */
    private String unionid;

    private String nickname;

    private String avatarUrl;

    /** 状态整数值，见 UserStatus.dbValue */
    private Integer status;

    private Instant createdAt;

    private Instant updatedAt;
}