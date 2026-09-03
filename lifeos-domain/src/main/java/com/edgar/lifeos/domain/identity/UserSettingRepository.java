package com.edgar.lifeos.domain.identity;

import java.util.Optional;

/**
 * 用户设置仓储契约。
 */
public interface UserSettingRepository {

    UserSetting save(UserSetting setting);

    Optional<UserSetting> findByUserId(Long userId);
}