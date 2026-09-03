package com.edgar.lifeos.domain.identity;

/**
 * 用户状态。
 */
public enum UserStatus {

    /** 正常 */
    ACTIVE(1),

    /** 禁用 */
    DISABLED(0);

    private final int dbValue;

    UserStatus(int dbValue) {
        this.dbValue = dbValue;
    }

    public int getDbValue() {
        return dbValue;
    }

    public static UserStatus fromDbValue(int v) {
        for (UserStatus s : values()) {
            if (s.dbValue == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知用户状态: " + v);
    }
}