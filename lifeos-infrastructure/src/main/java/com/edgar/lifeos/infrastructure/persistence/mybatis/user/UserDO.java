package com.edgar.lifeos.infrastructure.persistence.mybatis.user;

import com.edgar.lifeos.domain.identity.UserStatus;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表（life_user）数据库对象。
 *
 * <p>DO 与领域模型共享领域枚举：{@code status} 直接使用领域层
 * {@link UserStatus}，数据库中如何存储（TINYINT 1/0）完全由
 * infrastructure 的 {@code UserStatusTypeHandler} 负责，DO 不感知。</p>
 */
@Data
@NoArgsConstructor
public class UserDO implements Serializable {

    private Long id;

    /** 微信 openid，同一公众号/小程序下唯一 */
    private String openid;

    /** 微信 unionid（跨公众号/小程序唯一，可空） */
    private String unionid;

    private String nickname;

    private String avatarUrl;

    /** 用户状态（领域枚举），DB 表示见 UserStatusTypeHandler */
    private UserStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}