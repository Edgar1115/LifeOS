package com.edgar.lifeos.domain.identity;

import com.edgar.lifeos.common.domain.AggregateRoot;

/**
 * 用户个人设置。例如饮水目标等数据可用单独聚合，这里先占位保持最小骨架。
 */
public class UserSetting extends AggregateRoot {

    private Long userId;

    /** 通知开关等，暂不细化，仅保证聚合存在 */
    private boolean reminderEnabled;

    protected UserSetting() {
    }

    public static UserSetting create(Long userId) {
        UserSetting setting = new UserSetting();
        setting.userId = userId;
        setting.reminderEnabled = true;
        return setting;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }
}