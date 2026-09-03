package com.edgar.lifeos.common.security;

/**
 * 当前登录用户 ThreadLocal 持有者。
 */
public final class CurrentUserHolder {

    private static final ThreadLocal<CurrentUser> HOLDER = new ThreadLocal<>();

    private CurrentUserHolder() {
    }

    public static void set(CurrentUser user) {
        HOLDER.set(user);
    }

    public static CurrentUser get() {
        CurrentUser user = HOLDER.get();
        return user == null ? CurrentUser.ANONYMOUS : user;
    }

    /** 强制要求当前已登录，返回 userId。未登录抛 IllegalStateException。 */
    public static long requireUserId() {
        CurrentUser user = get();
        if (!user.isAuthenticated()) {
            throw new IllegalStateException("当前用户未登录");
        }
        return user.userId();
    }

    public static void clear() {
        HOLDER.remove();
    }
}