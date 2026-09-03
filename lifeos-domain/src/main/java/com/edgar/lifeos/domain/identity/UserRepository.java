package com.edgar.lifeos.domain.identity;

import java.util.Optional;

/**
 * 用户仓储契约。
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findByOpenid(String openid);

    Optional<User> findById(Long id);
}