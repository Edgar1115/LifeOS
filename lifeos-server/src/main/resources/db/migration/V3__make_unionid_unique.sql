-- =====================================================
-- V3: unionid 改为唯一索引
-- 说明：
--   * 微信 unionid 是同一开发者账号下跨应用（公众号/小程序）的统一身份标识，
--     一个自然人只能对应一个 LifeOS 用户 → 必须唯一。
--   * MySQL 唯一索引允许多个 NULL 共存，未绑定微信（unionid 为空）的行互不冲突。
-- =====================================================

ALTER TABLE `life_user`
    DROP INDEX `idx_user_unionid`,
    ADD UNIQUE KEY `uk_user_unionid` (`unionid`);