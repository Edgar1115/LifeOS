package com.edgar.lifeos.common.security;

/**
 * 当前登录用户上下文。
 *
 * <p>由认证 Filter 从 JWT 解析后写入 ThreadLocal，
 * 业务代码通过 {@link CurrentUserHolder} 获取。</p>
 */
public record CurrentUser(Long userId) {

    /** 匿名 / 未登录时使用 */
    public static final CurrentUser ANONYMOUS = new CurrentUser(null);

    public boolean isAuthenticated() {
        return userId != null;
    }
}