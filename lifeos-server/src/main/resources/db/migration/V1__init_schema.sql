-- =====================================================
-- V1: Phase 1 Identity — 用户表
-- 说明：
--   * 字段与 lifeos-domain User 聚合 + AggregateRoot 基类对齐
--   * status 存储 UserStatus.dbValue（ACTIVE=1, DISABLED=0）
--   * 排序规则统一 utf8mb4_0900_ai_ci（MySQL 8.0 默认，大小写不敏感）
-- =====================================================

CREATE TABLE `user` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `openid`     VARCHAR(64)  NOT NULL COMMENT '微信 openid，同一公众号/小程序下唯一',
    `unionid`    VARCHAR(64)  NULL COMMENT '微信 unionid（跨公众号/小程序唯一，可空）',
    `nickname`   VARCHAR(64)  NULL COMMENT '昵称',
    `avatar_url` VARCHAR(512) NULL COMMENT '头像 URL',
    `status`     TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1=ACTIVE 正常，0=DISABLED 禁用',
    `created_at` DATETIME(6)  NOT NULL COMMENT '创建时间（UTC）',
    `updated_at` DATETIME(6)  NOT NULL COMMENT '更新时间（UTC）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_openid` (`openid`),
    KEY `idx_user_unionid` (`unionid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户';